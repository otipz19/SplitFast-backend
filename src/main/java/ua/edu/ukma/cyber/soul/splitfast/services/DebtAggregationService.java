package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.events.AggregationRequiredEvent;
import ua.edu.ukma.cyber.soul.splitfast.events.FinishEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DebtAggregationService implements ApplicationRunner {

    private final ExpenseAggregationRequestRepository expenseAggregationRequestRepository;
    private final ActivityAggregationRequestRepository activityAggregationRequestRepository;

    private final ExpenseAggregatedDebtRepository expenseAggregatedDebtRepository;
    private final ActivityAggregatedDebtRepository activityAggregatedDebtRepository;
    private final ActivitiesGroupAggregatedDebtRepository activitiesGroupAggregatedDebtRepository;

    private final ExpenseMemberRepository expenseMemberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ContactService contactService;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public AggregationRequiredEvent<ExpenseEntity, Integer> prepareExpenseDebtAggregation(FinishEntityEvent<? extends ExpenseEntity, Integer> event) {
        ExpenseAggregationRequestEntity request = new ExpenseAggregationRequestEntity(event.getId());
        expenseAggregationRequestRepository.save(request);
        return new AggregationRequiredEvent<>(event.getEntity());
    }

    @Async("aggregationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void aggregateExpenseDebtAsync(AggregationRequiredEvent<? extends ExpenseEntity, Integer> event) {
        if (!shouldExpenseBeAggregated(event)) return;
        ExpenseEntity expense = event.getEntity();
        Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregated = computeExpenseDebt(expense);
        expenseAggregatedDebtRepository.saveAll(aggregated.values());
        updateActivityDebt(expense.getActivityId(), aggregated);
        contactService.updateContacts(aggregated);
    }

    private boolean shouldExpenseBeAggregated(AggregationRequiredEvent<? extends ExpenseEntity, Integer> event) {
        ExpenseAggregationRequestEntity request = expenseAggregationRequestRepository.findForUpdateByExpenseId(event.getId());
        if (request == null) return false;
        expenseAggregationRequestRepository.delete(request);
        return true;
    }

    private Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> computeExpenseDebt(ExpenseEntity expense) {
        List<ExpenseMemberEntity> shareholders = new ArrayList<>(), beneficiaries = new ArrayList<>();
        BigDecimal cost = splitMembers(expense, shareholders, beneficiaries);
        Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> debtMap = new HashMap<>();
        for (ExpenseMemberEntity beneficiary : beneficiaries) {
            for (ExpenseMemberEntity shareholder : shareholders) {
                if (beneficiary.getUserId() == shareholder.getUserId()) continue;
                TwoUsersAssociation association = new TwoUsersAssociation(beneficiary.getUserId(), shareholder.getUserId());
                ExpenseAggregatedDebtEntity aggregatedDebt = debtMap.computeIfAbsent(
                        association, ass -> new ExpenseAggregatedDebtEntity(ass, expense)
                );
                BigDecimal debt = computeDebt(beneficiary, shareholder, cost);
                if (beneficiary.getUserId() == association.getFirstUserId())
                    aggregatedDebt.setFirstDebt(debt);
                else
                    aggregatedDebt.setSecondDebt(debt);
            }
        }
        return debtMap;
    }

    private BigDecimal splitMembers(ExpenseEntity expense, List<ExpenseMemberEntity> shareholders, List<ExpenseMemberEntity> beneficiaries) {
        BigDecimal cost = BigDecimal.ZERO;
        for (ExpenseMemberEntity member : expenseMemberRepository.findAllByExpense(expense)) {
            switch (member.getType()) {
                case SHAREHOLDER -> {
                    shareholders.add(member);
                    cost = cost.add(member.getShare());
                }
                case BENEFICIARY -> beneficiaries.add(member);
            }
        }
        return cost;
    }

    private BigDecimal computeDebt(ExpenseMemberEntity beneficiary, ExpenseMemberEntity shareholder, BigDecimal cost) {
        return beneficiary.getShare()
                .multiply(shareholder.getShare())
                .divide(cost, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void updateActivityDebt(int activityId, Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregated) {
        activityAggregatedDebtRepository.lockActivityDebt(activityId);
        List<ActivityAggregatedDebtEntity> current = activityAggregatedDebtRepository.findAllByUsersAssociationInAndActivityId(aggregated.keySet(), activityId);
        activityAggregatedDebtRepository.saveAll(aggregate(current, ass -> new ActivityAggregatedDebtEntity(ass, activityId), aggregated.values()));
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public AggregationRequiredEvent<ActivityEntity, Integer> prepareActivityDebtAggregation(FinishEntityEvent<? extends ActivityEntity, Integer> event) {
        ActivityAggregationRequestEntity request = new ActivityAggregationRequestEntity(event.getId());
        activityAggregationRequestRepository.save(request);
        return new AggregationRequiredEvent<>(event.getEntity());
    }

    @Async("aggregationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void aggregateActivityDebtAsync(AggregationRequiredEvent<? extends ActivityEntity, Integer> event) {
        if (!shouldActivityBeAggregated(event)) return;
        ActivityEntity activity = event.getEntity();
        List<ActivityAggregatedDebtEntity> toAggregate = activityAggregatedDebtRepository.findByActivityId(activity.getId());
        List<TwoUsersAssociation> associations = toAggregate.stream().map(AggregatedDebt::getUsersAssociation).toList();
        activitiesGroupAggregatedDebtRepository.lockActivitiesGroupDebt(activity.getActivitiesGroupId());
        List<ActivitiesGroupAggregatedDebtEntity> current = activitiesGroupAggregatedDebtRepository.findAllByUsersAssociationInAndActivitiesGroupId(associations, activity.getActivitiesGroupId());
        activitiesGroupAggregatedDebtRepository.saveAll(aggregate(current, ass -> new ActivitiesGroupAggregatedDebtEntity(ass, activity.getActivitiesGroupId()), toAggregate));
    }

    private boolean shouldActivityBeAggregated(AggregationRequiredEvent<? extends ActivityEntity, Integer> event) {
        int activityId = event.getId();
        if (expenseAggregationRequestRepository.existsByActivityId(activityId)) {
            log.debug("Cannot aggregate activity {} because expense aggregation is not finished yet", activityId);
            eventPublisher.publishEvent(event);
            return false;
        }
        ActivityAggregationRequestEntity request = activityAggregationRequestRepository.findForUpdateByActivityId(activityId);
        if (request == null) return false;
        activityAggregationRequestRepository.delete(request);
        return true;
    }

    private <T extends AggregatedDebt> Collection<T> aggregate(Collection<T> current, Function<TwoUsersAssociation, T> supplier, Collection<? extends AggregatedDebt> toAggregate) {
        Map<TwoUsersAssociation, T> map = current.stream().collect(Collectors.toMap(AggregatedDebt::getUsersAssociation, Function.identity()));
        for (AggregatedDebt newData : toAggregate) {
            T aggregated = map.computeIfAbsent(newData.getUsersAssociation(), supplier);
            BigDecimal firstDebt = aggregated.getFirstDebt().add(newData.getFirstDebt());
            BigDecimal secondDebt = aggregated.getSecondDebt().add(newData.getSecondDebt());
            aggregated.setFirstDebt(firstDebt);
            aggregated.setSecondDebt(secondDebt);
        }
        return map.values();
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        expenseAggregationRequestRepository.findUnprocessedExpenses().forEach(this::publishAggregationRequiredEvent);
        activityAggregationRequestRepository.findUnprocessedActivities().forEach(this::publishAggregationRequiredEvent);
    }

    private void publishAggregationRequiredEvent(IGettableById<Integer> entity) {
        log.warn("Found unprocessed aggregation request for entity {} with id {}", entity.getClass().getSimpleName(), entity.getId());
        eventPublisher.publishEvent(new AggregationRequiredEvent<>(entity));
    }
}

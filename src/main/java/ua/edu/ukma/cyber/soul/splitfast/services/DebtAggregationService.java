package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.events.FinishEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupAggregatedDebtRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityAggregatedDebtRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseAggregatedDebtRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebtAggregationService {

    private final ExpenseAggregatedDebtRepository expenseAggregatedDebtRepository;
    private final ActivityAggregatedDebtRepository activityAggregatedDebtRepository;
    private final ActivitiesGroupAggregatedDebtRepository activitiesGroupAggregatedDebtRepository;
    private final ContactService contactService;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void aggregateExpenseDebt(FinishEntityEvent<ExpenseEntity, Integer> event) {
        ExpenseEntity expense = event.getEntity();
        List<ExpenseMemberEntity> shareholders = new ArrayList<>();
        List<ExpenseMemberEntity> beneficiaries = new ArrayList<>();
        BigDecimal cost = splitMembers(expense, shareholders, beneficiaries);
        Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregated = aggregate(expense, cost, shareholders, beneficiaries);
        expenseAggregatedDebtRepository.saveAll(aggregated.values());
        contactService.updateContacts(aggregated);
        updateActivityDebt(expense.getActivityId(), aggregated);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void aggregateActivityDebt(FinishEntityEvent<ActivityEntity, Integer> event) {
        ActivityEntity activity = event.getEntity();
        List<ActivityAggregatedDebtEntity> toAggregate = activityAggregatedDebtRepository.findAllByActivity(activity);
        List<TwoUsersAssociation> associations = toAggregate.stream().map(AggregatedDebt::getUsersAssociation).toList();
        List<ActivitiesGroupAggregatedDebtEntity> current = activitiesGroupAggregatedDebtRepository.findAllByUsersAssociationInAndActivitiesGroupId(associations, activity.getActivitiesGroupId());
        activitiesGroupAggregatedDebtRepository.saveAll(aggregate(current, ass -> new ActivitiesGroupAggregatedDebtEntity(ass, activity.getActivitiesGroupId()), toAggregate));
    }

    private BigDecimal splitMembers(ExpenseEntity entity, List<ExpenseMemberEntity> shareholders, List<ExpenseMemberEntity> beneficiaries) {
        BigDecimal cost = BigDecimal.ZERO;
        for (ExpenseMemberEntity member : entity.getMembers()) {
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

    private Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregate(ExpenseEntity expense, BigDecimal cost, List<ExpenseMemberEntity> shareholders, List<ExpenseMemberEntity> beneficiaries) {
        Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> debtMap = new HashMap<>();
        for (ExpenseMemberEntity beneficiary : beneficiaries) {
            for (ExpenseMemberEntity shareholder : shareholders) {
                if (beneficiary.getUserId() == shareholder.getUserId()) continue;
                TwoUsersAssociation association = new TwoUsersAssociation(beneficiary.getUserId(), shareholder.getUserId());
                ExpenseAggregatedDebtEntity aggregatedDebt = debtMap.computeIfAbsent(
                        association, ass -> new ExpenseAggregatedDebtEntity(ass, expense)
                );
                BigDecimal debt = beneficiary.getShare()
                        .multiply(shareholder.getShare())
                        .divide(cost, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP);
                if (beneficiary.getUserId() == association.getFirstUserId())
                    aggregatedDebt.setFirstDebt(debt);
                else
                    aggregatedDebt.setSecondDebt(debt);
            }
        }
        return debtMap;
    }

    private void updateActivityDebt(int activityId, Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregated) {
        List<ActivityAggregatedDebtEntity> current = activityAggregatedDebtRepository.findAllByUsersAssociationInAndActivityId(aggregated.keySet(), activityId);
        activityAggregatedDebtRepository.saveAll(aggregate(current, ass -> new ActivityAggregatedDebtEntity(ass, activityId), aggregated.values()));
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

}

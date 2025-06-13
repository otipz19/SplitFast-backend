package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.HonoraryUsersDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.StatisticsLevelDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.mappers.StatisticsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.*;
import ua.edu.ukma.cyber.soul.splitfast.validators.StatisticsValidator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsMapper mapper;
    private final StatisticsValidator validator;

    private final ExpenseAggregatedDebtRepository expenseAggregatedDebtRepository;
    private final ActivityAggregatedDebtRepository activityAggregatedDebtRepository;
    private final ActivitiesGroupAggregatedDebtRepository activitiesGroupAggregatedDebtRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @SerializableTransaction
    public List<DebtDistributionDto> getDebtDistribution(StatisticsLevelDto statisticsLevel, int aggregateId) {
        validator.validForView(statisticsLevel, aggregateId);
        List<? extends AggregatedDebt> aggregatedDebts = findFull(statisticsLevel, aggregateId);
        return mapper.toDebtDistribution(aggregatedDebts);
    }

    private List<? extends AggregatedDebt> findFull(StatisticsLevelDto statisticsLevel, int aggregateId) {
        return switch (statisticsLevel) {
            case EXPENSE -> expenseAggregatedDebtRepository.findFullByExpenseId(aggregateId);
            case ACTIVITY -> activityAggregatedDebtRepository.findFullByActivityId(aggregateId);
            case ACTIVITIES_GROUP -> activitiesGroupAggregatedDebtRepository.findFullByActivitiesGroupId(aggregateId);
        };
    }

    @SerializableTransaction
    public HonoraryUsersDto getHonoraryUsers(StatisticsLevelDto statisticsLevel, int aggregateId) {
        validator.validForView(statisticsLevel, aggregateId);
        List<? extends AggregatedDebt> aggregatedDebts = find(statisticsLevel, aggregateId);
        if (aggregatedDebts.isEmpty())
            return new HonoraryUsersDto(null, BigDecimal.ZERO, null, BigDecimal.ZERO);
        return computeHonoraryUsers(aggregatedDebts);
    }

    private List<? extends AggregatedDebt> find(StatisticsLevelDto statisticsLevel, int aggregateId) {
        return switch (statisticsLevel) {
            case EXPENSE -> expenseAggregatedDebtRepository.findByExpenseId(aggregateId);
            case ACTIVITY -> activityAggregatedDebtRepository.findByActivityId(aggregateId);
            case ACTIVITIES_GROUP -> activitiesGroupAggregatedDebtRepository.findByActivitiesGroupId(aggregateId);
        };
    }

    private HonoraryUsersDto computeHonoraryUsers(List<? extends AggregatedDebt> aggregatedDebts) {
        Map<Integer, BigDecimal> borrowings = new HashMap<>(), lendings = new HashMap<>();
        for (AggregatedDebt aggregatedDebt : aggregatedDebts) {
            BigDecimal first = aggregatedDebt.getFirstDebt();
            BigDecimal second = aggregatedDebt.getSecondDebt();
            int cmp = first.compareTo(second);
            if (cmp > 0) {
                BigDecimal diff = first.subtract(second);
                borrowings.merge(aggregatedDebt.getUsersAssociation().getFirstUserId(), diff, BigDecimal::add);
                lendings.merge(aggregatedDebt.getUsersAssociation().getSecondUserId(), diff, BigDecimal::add);
            } else if (cmp < 0) {
                BigDecimal diff = second.subtract(first);
                borrowings.merge(aggregatedDebt.getUsersAssociation().getSecondUserId(), diff, BigDecimal::add);
                lendings.merge(aggregatedDebt.getUsersAssociation().getFirstUserId(), diff, BigDecimal::add);
            }
        }
        Entry maxBorrowing = findMaxEntry(borrowings);
        Entry maxLending = findMaxEntry(lendings);
        return mapper.toHonoraryUsers(maxBorrowing.user(), maxBorrowing.amount(), maxLending.user(), maxLending.amount());
    }

    private Entry findMaxEntry(Map<Integer, BigDecimal> map) {
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> new Entry(userRepository.findById(e.getKey()).orElseThrow(), e.getValue()))
                .orElseThrow(IllegalStateException::new);
    }

    private record Entry(UserEntity user, BigDecimal amount) {}

    @SerializableTransaction
    public BigDecimal getExpensesCost(StatisticsLevelDto statisticsLevel, int aggregateId) {
        validator.validForView(statisticsLevel, aggregateId);
        List<Integer> expenseIds = findExpenseIds(statisticsLevel, aggregateId);
        if (expenseIds.isEmpty()) return BigDecimal.ZERO;
        return expenseRepository.calculateExpensesTotalCost(expenseIds);
    }

    private List<Integer> findExpenseIds(StatisticsLevelDto statisticsLevel, int aggregateId) {
        return switch (statisticsLevel) {
            case EXPENSE -> expenseRepository
                            .findById(aggregateId)
                            .filter(e -> e.getTimeFinished() != null)
                            .map(e -> List.of(e.getId()))
                            .orElse(List.of());
            case ACTIVITY -> expenseRepository.findFinishedExpenseIdsByActivityId(aggregateId);
            case ACTIVITIES_GROUP -> expenseRepository.findFinishedExpenseIdsByActivitiesGroupId(aggregateId);
        };
    }
}

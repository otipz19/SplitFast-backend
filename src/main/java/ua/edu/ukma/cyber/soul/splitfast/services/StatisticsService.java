package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.StatisticsLevelDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.mappers.StatisticsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupAggregatedDebtRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityAggregatedDebtRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseAggregatedDebtRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.StatisticsValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsMapper mapper;
    private final StatisticsValidator validator;
    private final ExpenseAggregatedDebtRepository expenseAggregatedDebtRepository;
    private final ActivityAggregatedDebtRepository activityAggregatedDebtRepository;
    private final ActivitiesGroupAggregatedDebtRepository activitiesGroupAggregatedDebtRepository;

    @SerializableTransaction
    public List<DebtDistributionDto> getDebtDistribution(StatisticsLevelDto statisticsLevel, int aggregateId) {
        validator.validForView(statisticsLevel, aggregateId);
        List<? extends AggregatedDebt> aggregatedDebts = switch (statisticsLevel) {
            case EXPENSE -> expenseAggregatedDebtRepository.findFullByExpenseId(aggregateId);
            case ACTIVITY -> activityAggregatedDebtRepository.findFullByActivityId(aggregateId);
            case ACTIVITIES_GROUP -> activitiesGroupAggregatedDebtRepository.findFullByActivitiesGroupId(aggregateId);
        };
        return mapper.toResponses(aggregatedDebts);
    }
}

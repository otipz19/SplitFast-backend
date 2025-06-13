package ua.edu.ukma.cyber.soul.splitfast.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.StatisticsLevelDto;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;

@Component
@RequiredArgsConstructor
public class StatisticsValidator {

    private final ExpenseValidator expenseValidator;
    private final ExpenseRepository expenseRepository;
    private final ActivityValidator activityValidator;
    private final ActivityRepository activityRepository;
    private final ActivitiesGroupValidator activitiesGroupValidator;
    private final ActivitiesGroupRepository activitiesGroupRepository;

    public void validForView(StatisticsLevelDto statisticsLevel, int aggregateId) {
        switch (statisticsLevel) {
            case EXPENSE -> expenseValidator.validForView(getById(aggregateId, expenseRepository));
            case ACTIVITY -> activityValidator.validForView(getById(aggregateId, activityRepository));
            case ACTIVITIES_GROUP -> activitiesGroupValidator.validForView(getById(aggregateId, activitiesGroupRepository));
        }
    }

    private <E> E getById(int id, IRepository<E, Integer> repository) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
}

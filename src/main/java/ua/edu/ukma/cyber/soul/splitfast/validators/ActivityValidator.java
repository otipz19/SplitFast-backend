package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;

import java.util.List;

@Component
public class ActivityValidator extends BaseValidator<ActivityEntity> {

    private final ExpenseRepository expenseRepository;
    private final ActivitiesGroupUtils activitiesGroupUtils;
    private final ActivityUtils activityUtils;

    public ActivityValidator(Validator validator, SecurityUtils securityUtils, ExpenseRepository expenseRepository, ActivitiesGroupUtils activitiesGroupUtils, ActivityUtils activityUtils) {
        super(validator, securityUtils);
        this.expenseRepository = expenseRepository;
        this.activitiesGroupUtils = activitiesGroupUtils;
        this.activityUtils = activityUtils;
    }

    @Override
    public void validForView(List<ActivityEntity> entities) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        entities.stream()
                .map(ActivityEntity::getActivitiesGroupId)
                .distinct()
                .filter(groupId -> !activitiesGroupUtils.isCurrentUserMemberOf(groupId))
                .findAny()
                .ifPresent(i -> { throw new ForbiddenException(); });
    }

    @Override
    public void validForView(ActivityEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activitiesGroupUtils.isCurrentUserMemberOf(entity.getActivitiesGroupId()))
            throw new ForbiddenException();
    }

    @Override
    public void validForCreate(ActivityEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            throw new ValidationException("error.activity.member.admin");
        if (!activitiesGroupUtils.isCurrentUserMemberOf(entity.getActivitiesGroup()))
            throw new ForbiddenException();
        validateData(entity);
    }

    @Override
    public void validForUpdate(ActivityEntity entity) {
        requireAdminOrOwner(entity);
        validateData(entity);
        requireNotFinished(entity);
    }

    @Override
    public void validForDelete(ActivityEntity entity) {
        requireAdminOrOwner(entity);
        requireNotFinished(entity);
        if (expenseRepository.existsByActivity(entity))
            throw new ValidationException("error.activity.has-expenses");
    }

    private void requireAdminOrOwner(ActivityEntity entity) {
        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN) && !activityUtils.isCurrentUserOwnerOf(entity))
            throw new ForbiddenException();
    }

    private void requireNotFinished(ActivityEntity entity) {
        if (activityUtils.isFinished(entity))
            throw new ValidationException("error.activity.finished");
    }
}

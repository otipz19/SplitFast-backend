package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;

import java.util.List;

@Component
public class ActivityValidator extends BaseValidator<ActivityEntity> {

    private final ActivitiesGroupUtils activitiesGroupUtils;
    private final ActivityUtils activityUtils;

    public ActivityValidator(Validator validator, SecurityUtils securityUtils, ActivitiesGroupUtils activitiesGroupUtils, ActivityUtils activityUtils) {
        super(validator, securityUtils);
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
        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN) && !activityUtils.isCurrentUserOwnerOf(entity))
            throw new ForbiddenException();
        validateData(entity);
    }

    @Override
    protected void validateData(ActivityEntity entity) {
        super.validateData(entity);
        if (activityUtils.isFinished(entity))
            throw new ValidationException("error.activity.finished");
    }
}

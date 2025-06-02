package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;

@Component
public class ActivitiesGroupValidator extends BaseValidator<ActivitiesGroupEntity> {

    private final ActivitiesGroupUtils activitiesGroupUtils;

    public ActivitiesGroupValidator(Validator validator, SecurityUtils securityUtils, ActivitiesGroupUtils activitiesGroupUtils) {
        super(validator, securityUtils);
        this.activitiesGroupUtils = activitiesGroupUtils;
    }

    @Override
    public void validForView(ActivitiesGroupEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activitiesGroupUtils.isCurrentUserMemberOf(entity))
            throw new ForbiddenException();
    }

    @Override
    public void validForCreate(ActivitiesGroupEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            throw new ValidationException("error.activities-group.owner.admin");
        validateData(entity);
    }

    @Override
    public void validForUpdate(ActivitiesGroupEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activitiesGroupUtils.isCurrentUserOwnerOf(entity))
            throw new ForbiddenException();
        validateData(entity);
    }
}

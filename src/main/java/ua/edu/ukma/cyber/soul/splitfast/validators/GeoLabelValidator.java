package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;

import java.util.List;

@Component
public class GeoLabelValidator extends BaseValidator<GeoLabelEntity> {

    private final SecurityUtils securityUtils;
    private final ActivitiesGroupUtils activitiesGroupUtils;

    public GeoLabelValidator(Validator validator, SecurityUtils securityUtils, ActivitiesGroupUtils activitiesGroupUtils) {
        super(validator, securityUtils);
        this.securityUtils = securityUtils;
        this.activitiesGroupUtils = activitiesGroupUtils;
    }

    @Override
    public void validForView(List<GeoLabelEntity> entities) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        entities.stream()
                .map(GeoLabelEntity::getActivitiesGroupId)
                .distinct()
                .filter(groupId -> !activitiesGroupUtils.isCurrentUserMemberOf(groupId))
                .findAny()
                .ifPresent(i -> { throw new ForbiddenException(); });
    }

    @Override
    public void validForView(GeoLabelEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activitiesGroupUtils.isCurrentUserMemberOf(entity.getActivitiesGroupId()))
            throw new ForbiddenException();
    }

    @Override
    public void validForCreate(GeoLabelEntity entity) {
        if (entity.getOwner().getRole() != UserRole.USER)
            throw new ValidationException("error.geo-label.owner.admin");
        if (!activitiesGroupUtils.isCurrentUserMemberOf(entity.getActivitiesGroup()))
            throw new ForbiddenException();
        validateData(entity);
    }

    @Override
    public void validForUpdate(GeoLabelEntity entity) {
        requireAdminOrOwner(entity);
        validateData(entity);
    }

    @Override
    public void validForDelete(GeoLabelEntity entity) {
        requireAdminOrOwner(entity);
    }

    private void requireAdminOrOwner(GeoLabelEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (securityUtils.getCurrentUserId() != entity.getOwnerId())
            throw new ForbiddenException();
    }
}

package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;

import java.util.List;
import java.util.Objects;

@Component
public class GeoLabelValidator extends BaseValidator<GeoLabelEntity> {

    private final SecurityUtils securityUtils;
    private final ActivityUtils activityUtils;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public GeoLabelValidator(Validator validator, SecurityUtils securityUtils, ActivityUtils activityUtils,
                             ActivityRepository activityRepository, UserRepository userRepository) {
        super(validator, securityUtils);
        this.securityUtils = securityUtils;
        this.activityUtils = activityUtils;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void validForView(List<GeoLabelEntity> entities) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        entities.stream()
                .map(GeoLabelEntity::getActivityId)
                .filter(Objects::nonNull)
                .distinct()
                .filter(activityId -> !activityUtils.isCurrentUserMemberOf(activityId))
                .findAny()
                .ifPresent(i -> { throw new ForbiddenException(); });
    }

    @Override
    public void validForView(GeoLabelEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activityUtils.isCurrentUserMemberOf(entity.getActivityId()))
            throw new ForbiddenException();
    }

    @Override
    public void validForCreate(GeoLabelEntity entity) {
        validateData(entity);

        if (!userRepository.existsById(entity.getCreatorId())) {
            throw new ValidationException("error.geo-label.creator.not-exists");
        }
        if (!activityRepository.existsById(entity.getActivityId())) {
            throw new ValidationException("error.geo-label.activity.not-exists");
        }

        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN)) {
            boolean isCurrentUserCreator = entity.getCreator() != null && Objects.equals(entity.getCreator().getId(), securityUtils.getCurrentUser().getId());
            boolean isCurrentUserActivityMember = activityUtils.isCurrentUserMemberOf(entity.getActivityId());

            if (!isCurrentUserCreator && !isCurrentUserActivityMember) {
                throw new ForbiddenException();
            }
        }
    }

    @Override
    public void validForUpdate(GeoLabelEntity entity) {
        validateData(entity);

        if (!userRepository.existsById(entity.getCreatorId())) {
            throw new ValidationException("error.geo-label.creator.not-exists");
        }
        if (!activityRepository.existsById(entity.getActivityId())) {
            throw new ValidationException("error.geo-label.activity.not-exists");
        }

        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN)) {
            boolean isCurrentUserCreator = entity.getCreator() != null && Objects.equals(entity.getCreator().getId(), securityUtils.getCurrentUser().getId());
            boolean isCurrentUserActivityMember = activityUtils.isCurrentUserMemberOf(entity.getActivityId());

            if (!isCurrentUserCreator && !isCurrentUserActivityMember) {
                throw new ForbiddenException();
            }
        }
    }

    @Override
    public void validForDelete(GeoLabelEntity entity) {
        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN)) {
            boolean isCurrentUserCreator = entity.getCreator() != null && Objects.equals(entity.getCreator().getId(), securityUtils.getCurrentUser().getId());
            boolean isCurrentUserActivityMember = activityUtils.isCurrentUserMemberOf(entity.getActivityId());

            if (!isCurrentUserCreator && !isCurrentUserActivityMember) {
                throw new ForbiddenException();
            }
        }
    }
}

package ua.edu.ukma.cyber.soul.splitfast.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;

@Component
@RequiredArgsConstructor
public class ActivityMemberValidator {

    private final ActivityUtils activityUtils;
    private final ActivitiesGroupUtils activitiesGroupUtils;
    private final SecurityUtils securityUtils;

    public void validForJoin(ActivityEntity activity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            throw new ValidationException("error.activity.member.admin");
        if (!activitiesGroupUtils.isCurrentUserMemberOf(activity.getActivitiesGroupId()))
            throw new ForbiddenException();
        if (activityUtils.isFinished(activity))
            throw new ValidationException("error.activity.finished");
        if (activityUtils.isCurrentUserMemberOf(activity))
            throw new ValidationException("error.activity.already-member");
    }
}

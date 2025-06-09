package ua.edu.ukma.cyber.soul.splitfast.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivityCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;

@Component
@RequiredArgsConstructor
public class ActivitiesGroupMemberValidator {

    private final CriteriaRepository criteriaRepository;
    private final ActivitiesGroupUtils activitiesGroupUtils;
    private final SecurityUtils securityUtils;

    public void validForEnd(ActivitiesGroupMemberEntity membership) {
        if (membership.getUserId() != securityUtils.getCurrentUserId())
            requireAdminOrOwner(membership);
        if (membership.isOwner())
            throw new ValidationException("error.activities-group-member.is-owner");
        if (isMemerOfActivitiesWithinGroup(membership))
            throw new ValidationException("error.activities-group-member.member-of-activities");
    }

    private void requireAdminOrOwner(ActivitiesGroupMemberEntity membership) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activitiesGroupUtils.isCurrentUserOwnerOf(membership.getActivitiesGroupId()))
            throw new ForbiddenException();
    }

    private boolean isMemerOfActivitiesWithinGroup(ActivitiesGroupMemberEntity membership) {
        ActivityCriteriaDto criteriaDto = new ActivityCriteriaDto();
        criteriaDto.setUserId(membership.getUserId());
        ActivityCriteria criteria = new ActivityCriteria(criteriaDto, membership.getActivitiesGroupId());
        return criteriaRepository.count(criteria) > 0;
    }
}

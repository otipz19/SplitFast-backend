package ua.edu.ukma.cyber.soul.splitfast.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseParticipationCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseParticipationTypeDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ExpenseCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityMemberValidator {

    private final CriteriaRepository criteriaRepository;
    private final ActivityUtils activityUtils;
    private final ActivitiesGroupUtils activitiesGroupUtils;
    private final SecurityUtils securityUtils;

    public void validForJoin(ActivityEntity activity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            throw new ValidationException("error.activity.member.admin");
        if (!activitiesGroupUtils.isCurrentUserMemberOf(activity.getActivitiesGroupId()))
            throw new ForbiddenException();
        requireNotFinishedActivity(activity);
        if (activityUtils.isCurrentUserMemberOf(activity))
            throw new ValidationException("error.activity.already-member");
    }

    public void validForLeave(ActivityMemberEntity member) {
        requireNotFinishedActivity(member.getActivity());
        if (member.isOwner())
            throw new ValidationException("error.activity.member-is-owner");
        if (isMemberOfExpensesWithinActivity(member))
            throw new ValidationException("error.activity.member-of-expenses");
    }

    private void requireNotFinishedActivity(ActivityEntity activity) {
        if (activityUtils.isFinished(activity))
            throw new ValidationException("error.activity.finished");
    }

    private boolean isMemberOfExpensesWithinActivity(ActivityMemberEntity member) {
        ExpenseParticipationCriteriaDto participationCriteriaDto = new ExpenseParticipationCriteriaDto();
        participationCriteriaDto.setUserId(member.getUserId());
        participationCriteriaDto.setTypes(List.of(ExpenseParticipationTypeDto.ANY));
        ExpenseCriteriaDto criteriaDto = new ExpenseCriteriaDto();
        criteriaDto.setExpenseParticipation(participationCriteriaDto);
        ExpenseCriteria criteria = new ExpenseCriteria(criteriaDto, member.getActivityId());
        return criteriaRepository.count(criteria) > 0;
    }
}

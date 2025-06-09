package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupInvitationRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivitiesGroupUtils;

@Component
public class ActivitiesGroupInvitationValidator extends BaseValidator<ActivitiesGroupInvitationEntity> {

    private final ActivitiesGroupInvitationRepository invitationRepository;
    private final ActivitiesGroupUtils activitiesGroupUtils;

    public ActivitiesGroupInvitationValidator(Validator validator, SecurityUtils securityUtils, ActivitiesGroupInvitationRepository invitationRepository, ActivitiesGroupUtils activitiesGroupUtils) {
        super(validator, securityUtils);
        this.invitationRepository = invitationRepository;
        this.activitiesGroupUtils = activitiesGroupUtils;
    }

    @Override
    public void validForView(ActivitiesGroupInvitationEntity entity) {
        if (isCurrentUserParticipantOfInvitation(entity))
            return;
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }

    @Override
    public void validForCreate(ActivitiesGroupInvitationEntity entity) {
        if (!activitiesGroupUtils.isCurrentUserOwnerOf(entity.getActivitiesGroup()))
            throw new ForbiddenException();
        validateData(entity);
    }

    @Override
    public void validForDelete(ActivitiesGroupInvitationEntity entity) {
        if (!isCurrentUserParticipantOfInvitation(entity))
            throw new ForbiddenException();
    }

    public void validForAccept(ActivitiesGroupInvitationEntity invitation) {
        if (invitation.getUsersAssociation().getToUserId() != securityUtils.getCurrentUserId())
            throw new ForbiddenException();
    }

    @Override
    protected void validateData(ActivitiesGroupInvitationEntity entity) {
        super.validateData(entity);
        UserEntity toUser = entity.getUsersAssociation().getToUser();
        if (toUser.getRole() != UserRole.USER)
            throw new ValidationException("error.activities-group-invitation.to-user.invalid-role");
        if (invitationRepository.existsByToUserAndActivitiesGroup(toUser, entity.getActivitiesGroup()))
            throw new ValidationException("error.activities-group-invitation.already-sent");
        if (activitiesGroupUtils.isUserMemberOf(toUser, entity.getActivitiesGroup()))
            throw new ValidationException("error.activities-group-invitation.already-member");
    }

    private boolean isCurrentUserParticipantOfInvitation(ActivitiesGroupInvitationEntity invitation) {
        int currentUserId = securityUtils.getCurrentUserId();
        return invitation.getUsersAssociation().getFromUserId() == currentUserId
                || invitation.getUsersAssociation().getToUserId() == currentUserId;
    }
}

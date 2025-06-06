package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateActivitiesGroupInvitationDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class ActivitiesGroupInvitationMerger implements IMerger<ActivitiesGroupInvitationEntity, CreateActivitiesGroupInvitationDto, Void> {

    private final UserRepository userRepository;
    private final ActivitiesGroupRepository activitiesGroupRepository;

    @Override
    public void mergeForCreate(ActivitiesGroupInvitationEntity entity, CreateActivitiesGroupInvitationDto view) {
        UserEntity toUser = userRepository.findById(view.getToUserId())
                .orElseThrow(() -> new ValidationException("error.activities-group-invitation.to-user.not-exists"));
        TwoUsersDirectedAssociation association = new TwoUsersDirectedAssociation();
        association.setToUser(toUser);
        association.setToUserId(toUser.getId());
        entity.setUsersAssociation(association);

        ActivitiesGroupEntity activitiesGroup = activitiesGroupRepository.findById(view.getActivitiesGroupId())
                .orElseThrow(() -> new ValidationException("error.activities-group-invitation.activities-group.not-exists"));
        entity.setActivitiesGroup(activitiesGroup);
    }

    @Override
    public void mergeForUpdate(ActivitiesGroupInvitationEntity entity, Void view) {
        throw new UnsupportedOperationException();
    }
}

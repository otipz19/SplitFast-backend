package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class ContactRequestMerger implements IMerger<ContactRequestEntity, CreateContactRequestDto> {

    private final UserRepository userRepository;

    @Override
    public void mergeForCreate(ContactRequestEntity entity, CreateContactRequestDto view) {
        UserEntity toUser = userRepository.findById(view.getToUserId())
                .orElseThrow(() -> new ValidationException("error.contact-request.to-user.not-exists"));
        TwoUsersDirectedAssociation association = new TwoUsersDirectedAssociation();
        association.setToUser(toUser);
        association.setToUserId(toUser.getId());
        entity.setUsersAssociation(association);
    }

    @Override
    public void mergeForUpdate(ContactRequestEntity entity, CreateContactRequestDto view) {
        throw new UnsupportedOperationException();
    }
}

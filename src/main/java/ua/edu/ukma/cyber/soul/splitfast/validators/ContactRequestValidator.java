package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
public class ContactRequestValidator extends BaseValidator<ContactRequestEntity> {

    private final ContactRequestRepository contactRequestRepository;
    private final ContactRepository contactRepository;

    public ContactRequestValidator(
            Validator validator,
            SecurityUtils securityUtils,
            ContactRequestRepository contactRequestRepository,
            ContactRepository contactRepository
    ) {
        super(validator, securityUtils);
        this.contactRequestRepository = contactRequestRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public void validForView(ContactRequestEntity entity) {
        if (isCurrentUserParticipantOfContactRequest(entity))
            return;
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }

    @Override
    public void validForDelete(ContactRequestEntity request) {
        if (!isCurrentUserParticipantOfContactRequest(request))
            throw new ForbiddenException();
    }

    public void validForAccept(ContactRequestEntity request) {
        int currentUserId = securityUtils.getCurrentUser().getId();
        if (request.getUsersAssociation().getToUserId() != currentUserId)
            throw new ForbiddenException();
    }

    @Override
    protected void validateData(ContactRequestEntity entity) {
        super.validateData(entity);
        if (!areParticipantsDefaultUsers(entity))
            throw new ValidationException("error.contact-request.user.invalid-role");
        if (contactRequestRepository.existsByUsersAssociation(entity.getUsersAssociation()))
            throw new ValidationException("error.contact-request.already-exists");
        if (contactRepository.existsByUsersAssociation(entity.getUsersAssociation()))
            throw new ValidationException("error.contact.already-exists");
    }

    private boolean areParticipantsDefaultUsers(ContactRequestEntity request) {
        return request.getUsersAssociation().getFromUser().getRole() == UserRole.USER
                && request.getUsersAssociation().getToUser().getRole() == UserRole.USER;
    }

    private boolean isCurrentUserParticipantOfContactRequest(ContactRequestEntity request) {
        int currentUserId = securityUtils.getCurrentUser().getId();
        return request.getUsersAssociation().getFromUserId() == currentUserId
                || request.getUsersAssociation().getToUserId() == currentUserId;
    }
}
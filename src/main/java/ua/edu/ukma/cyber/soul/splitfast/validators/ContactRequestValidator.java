package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
public class ContactRequestValidator extends BaseValidator<ContactRequestEntity> {

    private final ContactRequestRepository contactRequestRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactRequestValidator(
            Validator validator,
            SecurityUtils securityUtils,
            ContactRequestRepository contactRequestRepository,
            ContactRepository contactRepository,
            UserRepository userRepository
    ) {
        super(validator, securityUtils);
        this.contactRequestRepository = contactRequestRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void validateData(ContactRequestEntity entity) {
        Integer fromUserId = entity.getFromUserId();
        Integer toUserId = entity.getToUserId();

        if (fromUserId.equals(toUserId)) {
            throw new ValidationException("Cannot send contact request to yourself.");
        }

        if (!userRepository.existsById(fromUserId)) {
            throw new ValidationException("User with ID " + fromUserId + " does not exist.");
        }
        if (!userRepository.existsById(toUserId)) {
            throw new ValidationException("User with ID " + toUserId + " does not exist.");
        }

        boolean contactExists = contactRepository.existsByIdFirstUserIdAndIdSecondUserId(fromUserId, toUserId)
                || contactRepository.existsByIdFirstUserIdAndIdSecondUserId(toUserId, fromUserId);
        if (contactExists) {
            throw new ValidationException("Contact already exists between users.");
        }

        boolean requestExists = contactRequestRepository.findByFromUserIdAndToUserId(fromUserId, toUserId).isPresent();
        if (requestExists) {
            throw new ValidationException("A contact request has already been sent.");
        }

        super.validateData(entity);
    }

    public void validateForAccept(ContactRequestEntity request) {
        validateData(request);
    }

}
package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

import java.util.Optional;

@Component
public class ContactValidator extends BaseValidator<ContactEntity> {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactValidator(Validator validator, SecurityUtils securityUtils,
                            ContactRepository contactRepository, UserRepository userRepository) {
        super(validator, securityUtils);
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public void validateForAccept(ContactEntity contact) {
        validateData(contact);
        validateNotSelfContact(contact);
        validateUsersExist(contact);
        validateContactNotExists(contact);
    }

    public void validateNotSelfContact(ContactEntity contact) {
        if (contact.getId().getFirstUserId() == contact.getId().getSecondUserId()) {
            throw new ValidationException("User cannot add themselves as a contact.");
        }
    }
    public void validateUsersExist(ContactEntity contact) {
        Optional<UserEntity> firstUser = userRepository.findById(contact.getId().getFirstUserId());
        Optional<UserEntity> secondUser = userRepository.findById(contact.getId().getSecondUserId());

        if (firstUser.isEmpty() || secondUser.isEmpty()) {
            throw new ValidationException("One or both users do not exist.");
        }
    }

    public void validateContactNotExists(ContactEntity contact) {
               boolean exists = contactRepository.existsByIdFirstUserIdAndIdSecondUserId(
                contact.getId().getFirstUserId(), contact.getId().getSecondUserId()
        ) || contactRepository.existsByIdFirstUserIdAndIdSecondUserId(
                contact.getId().getSecondUserId(), contact.getId().getFirstUserId()
        );

        if (exists) {
            throw new ValidationException("This contact already exists.");
        }
    }
}

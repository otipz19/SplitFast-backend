package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

import java.util.Optional;

@Component
public class ContactValidator extends BaseValidator<ContactEntity> {

    public ContactValidator(Validator validator, SecurityUtils securityUtils){
        super(validator, securityUtils);
    }

    @Override
    public void validForView(ContactEntity entity) {
        int currentUserId = securityUtils.getCurrentUser().getId();
        if (entity.getUsersAssociation().getFirstUserId() == currentUserId || entity.getUsersAssociation().getSecondUserId() == currentUserId)
            return;
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }
}

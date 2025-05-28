package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.utils.SecurityUtils;

@Component
public class UserValidator extends BaseValidator<UserEntity> {

    private final UserRepository userRepository;

    public UserValidator(Validator validator, SecurityUtils securityUtils, UserRepository userRepository) {
        super(validator, securityUtils);
        this.userRepository = userRepository;
    }

    public void validForRegister(UserEntity user) {
        validateData(user);
        validateUsername(user);
        validateRole(user);
    }

    private void validateUsername(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ValidationException("error.user.username.duplicate");
    }

    private void validateRole(UserEntity user) {
        switch (user.getRole()) {
            case ADMIN -> securityUtils.requireRole(UserRole.SUPER_ADMIN);
            case SUPER_ADMIN -> throw new ValidationException("error.user.role.superadmin");
        }
    }
}

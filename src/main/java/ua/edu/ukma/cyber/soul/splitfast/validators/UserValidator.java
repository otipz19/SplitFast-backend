package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
public class UserValidator extends BaseValidator<UserEntity> {

    private final UserRepository userRepository;

    public UserValidator(Validator validator, SecurityUtils securityUtils, UserRepository userRepository) {
        super(validator, securityUtils);
        this.userRepository = userRepository;
    }

    @Override
    public void validForView(UserEntity user) {
        if (user.getRole() == UserRole.USER)
            return;
        securityUtils.requireRole(UserRole.ADMIN, UserRole.SUPER_ADMIN);
    }

    @Override
    public void validForUpdate(UserEntity user) {
        int currentUserId = securityUtils.getCurrentUser().getId();
        if (user.getId() == currentUserId)
            return;
        securityUtils.requireRole(UserRole.ADMIN, UserRole.SUPER_ADMIN);
    }

    public void validForRegister(UserEntity user) {
        validateData(user);
        validateUsername(user);
        validateRole(user);
    }

    public void validForUpdatePassword(UserEntity user) {
        if (user.getRole() == UserRole.SUPER_ADMIN)
            throw new ValidationException("error.user.update-password.super-admin");
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

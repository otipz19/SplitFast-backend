package ua.edu.ukma.cyber.soul.splitfast.validators.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

public class TwoDifferentUsersValidator implements ConstraintValidator<DifferentUsers, TwoUsersAssociation> {

    @Override
    public boolean isValid(TwoUsersAssociation value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        return value.getFirstUserId() != value.getSecondUserId();
    }
}

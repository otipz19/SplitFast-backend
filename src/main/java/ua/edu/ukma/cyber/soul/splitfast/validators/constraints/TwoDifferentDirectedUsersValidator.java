package ua.edu.ukma.cyber.soul.splitfast.validators.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

public class TwoDifferentDirectedUsersValidator implements ConstraintValidator<DifferentUsers, TwoUsersDirectedAssociation> {

    @Override
    public boolean isValid(TwoUsersDirectedAssociation value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        return value.getToUserId() != value.getFromUserId();
    }
}

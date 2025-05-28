package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.utils.SecurityUtils;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public abstract class BaseValidator<E> implements IValidator<E> {
    
    protected final Validator validator;
    protected final SecurityUtils securityUtils;

    @Override
    public void validForView(E entity) {
        securityUtils.authorized();
    }

    @Override
    public void validForView(List<E> entities) {
        securityUtils.authorized();
    }

    @Override
    public void validForCreate(E entity) {
        securityUtils.authorized();
        validateData(entity);
    }

    @Override
    public void validForUpdate(E entity) {
        securityUtils.authorized();
        validateData(entity);
    }

    @Override
    public void validForDelete(E entity) {
        securityUtils.authorized();
    }

    protected void validateData(E entity) {
        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (violations != null && !violations.isEmpty())
            throw new ValidationException(violations);
    }
}

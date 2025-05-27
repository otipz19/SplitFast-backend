package ua.edu.ukma.cyber.soul.splitfast.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

@Getter
public class ValidationException extends BaseException {

    private Set<? extends ConstraintViolation<?>> violations;

    public ValidationException() {
        super("error.application.invalid-data");
        this.violations = Set.of();
    }

    public <T> ValidationException(Set<ConstraintViolation<T>> violations) {
        this();
        this.violations = violations;
    }
}

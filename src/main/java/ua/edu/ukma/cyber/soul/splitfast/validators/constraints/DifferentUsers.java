package ua.edu.ukma.cyber.soul.splitfast.validators.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TwoDifferentUsersValidator.class, TwoDifferentDirectedUsersValidator.class})
@Documented
public @interface DifferentUsers {

    String message() default "error.users-association.users.same";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

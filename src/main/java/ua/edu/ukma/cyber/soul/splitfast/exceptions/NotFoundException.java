package ua.edu.ukma.cyber.soul.splitfast.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {

    private String details;

    public NotFoundException() {
        super("error.application.no-resource");
    }

    public NotFoundException(Class<?> entityClass, String parameters) {
        this();
        this.details = String.format(
                "No entity of type '%s' with parameters: %s",
                entityClass.getSimpleName(), parameters
            );
    }

}

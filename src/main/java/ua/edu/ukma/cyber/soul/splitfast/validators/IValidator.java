package ua.edu.ukma.cyber.soul.splitfast.validators;

import java.util.List;

public interface IValidator<E> {

    void validForView(E entity);

    void validForView(List<E> entity);

    void validForCreate(E entity);

    void validForUpdate(E entity);

    void validForDelete(E entity);
}

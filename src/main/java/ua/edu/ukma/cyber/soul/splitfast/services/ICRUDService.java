package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.lang.NonNull;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

public interface ICRUDService<E extends IGettableById<I>, R, V, I extends Comparable<I>> {

    E getById(@NonNull I id);

    R getResponseById(@NonNull I id);

    I create(@NonNull V view);

    boolean update(@NonNull I id, @NonNull V view);

    void delete(@NonNull I id);
}

package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.lang.NonNull;
import ua.edu.ukma.cyber.soul.splitfast.criteria.Criteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.util.List;

public interface ICRUDService<E extends IGettableById<I>, CV, UV, I extends Comparable<I>> {

    E getById(@NonNull I id);

    List<E> getList(@NonNull Criteria<E, ?> criteria);

    long count(@NonNull Criteria<E, ?> criteria);

    I create(@NonNull CV view);

    boolean update(@NonNull I id, @NonNull UV view);

    void delete(@NonNull I id);
}

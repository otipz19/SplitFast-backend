package ua.edu.ukma.cyber.soul.splitfast.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@ToString
@EqualsAndHashCode(callSuper = true)
public class AggregationRequiredEvent<E extends IGettableById<I>, I extends Comparable<I>> extends EntityEvent<E, I> {

    public AggregationRequiredEvent(E entity) {
        super(entity);
    }
}

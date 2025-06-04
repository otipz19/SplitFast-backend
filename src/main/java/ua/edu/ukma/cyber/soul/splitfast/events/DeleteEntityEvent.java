package ua.edu.ukma.cyber.soul.splitfast.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DeleteEntityEvent<E, I> extends EntityEvent<E, I> {

    public DeleteEntityEvent(Class<E> entityClass, I id) {
        super(entityClass, id);
    }
}

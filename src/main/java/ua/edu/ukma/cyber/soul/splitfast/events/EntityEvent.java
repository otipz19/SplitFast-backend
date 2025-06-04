package ua.edu.ukma.cyber.soul.splitfast.events;

import lombok.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class EntityEvent<E, I> implements ResolvableTypeProvider {
    private final Class<E> entityClass;
    private final I id;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forClass(entityClass),
                ResolvableType.forInstance(id)
        );
    }
}

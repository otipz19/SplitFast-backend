package ua.edu.ukma.cyber.soul.splitfast.mappers;

public interface IShortResponseMapper<E, SR> {
    SR toShortResponse(E entity);
}

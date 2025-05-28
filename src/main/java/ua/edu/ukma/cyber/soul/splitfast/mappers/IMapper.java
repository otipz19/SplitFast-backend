package ua.edu.ukma.cyber.soul.splitfast.mappers;

public interface IMapper<E, R> {
    R toResponse(E entity);
}

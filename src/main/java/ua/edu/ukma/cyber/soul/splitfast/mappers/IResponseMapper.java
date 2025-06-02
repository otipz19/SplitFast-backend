package ua.edu.ukma.cyber.soul.splitfast.mappers;

public interface IResponseMapper<E, R> {
    R toResponse(E entity);
}

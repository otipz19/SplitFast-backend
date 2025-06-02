package ua.edu.ukma.cyber.soul.splitfast.mappers;

import java.util.List;

public interface IListResponseMapper<E, LR> {
    LR toListResponse(long total, List<E> items);
}

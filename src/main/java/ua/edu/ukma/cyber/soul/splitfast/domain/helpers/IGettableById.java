package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

public interface IGettableById<ID extends Comparable<ID>> {
    ID getId();
}

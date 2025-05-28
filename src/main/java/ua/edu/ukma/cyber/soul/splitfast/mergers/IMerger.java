package ua.edu.ukma.cyber.soul.splitfast.mergers;

public interface IMerger<E, V> {

    void mergeForCreate(E entity, V view);

    void mergeForUpdate(E entity, V view);
}

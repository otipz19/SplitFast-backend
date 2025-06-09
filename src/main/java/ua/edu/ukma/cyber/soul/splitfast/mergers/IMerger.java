package ua.edu.ukma.cyber.soul.splitfast.mergers;

public interface IMerger<E, CV, UV> {

    void mergeForCreate(E entity, CV view);

    void mergeForUpdate(E entity, UV view);
}

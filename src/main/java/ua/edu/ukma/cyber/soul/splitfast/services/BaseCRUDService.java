package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.criteria.Criteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class BaseCRUDService<E extends IGettableById<I>, V, I extends Comparable<I>> implements ICRUDService<E, V, I> {

    protected final IRepository<E, I> repository;
    protected final CriteriaRepository criteriaRepository;
    protected final IValidator<E> validator;
    protected final IMerger<E, V> merger;
    protected final ApplicationEventPublisher eventPublisher;
    protected final Class<E> entityClass;
    protected final Supplier<E> entitySupplier;

    @Transactional(readOnly = true)
    public E getByIdWithoutValidation(@NonNull I id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(entityClass, "id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public E getById(@NonNull I id) {
        E entity = getByIdWithoutValidation(id);
        validator.validForView(entity);
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> getList(@NonNull Criteria<E, ?> criteria) {
        List<E> entities = criteriaRepository.find(criteria);
        validator.validForView(entities);
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public long count(@NonNull Criteria<E, ?> criteria) {
        return criteriaRepository.count(criteria);
    }

    @Override
    @Transactional
    public I create(@NonNull V view) {
        return createEntity(view).getId();
    }

    @Transactional
    public E createEntity(@NonNull V view) {
        E entity = entitySupplier.get();
        merger.mergeForCreate(entity, view);
        postCreate(entity, view);
        validator.validForCreate(entity);
        return repository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public boolean update(@NonNull I id, @NonNull V view) {
        E entity = getByIdWithoutValidation(id);
        merger.mergeForUpdate(entity, view);
        postUpdate(entity, view);
        validator.validForUpdate(entity);
        repository.saveAndFlush(entity);
        return true;
    }

    @Override
    @Transactional
    public void delete(@NonNull I id) {
        E entity = getByIdWithoutValidation(id);
        validator.validForDelete(entity);
        eventPublisher.publishEvent(new DeleteEntityEvent<>(entityClass, id));
        repository.delete(entity);
    }

    protected void postCreate(@NonNull E entity, @NonNull V view) {}

    protected void postUpdate(@NonNull E entity, @NonNull V view) {}
}

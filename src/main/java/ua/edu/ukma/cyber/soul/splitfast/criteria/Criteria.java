package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.BaseCriteriaDto;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class Criteria<ENTITY, CRITERIA extends BaseCriteriaDto> {

    private final Class<ENTITY> entityClass;
    protected final CRITERIA criteria;

    public abstract List<Predicate> query(Root<ENTITY> root, CriteriaBuilder cb);

    public TypedQuery<ENTITY> createQuery(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = cb.createQuery(entityClass);

        Root<ENTITY> root = query.from(entityClass);
        query.select(root);
        query.distinct(true);
        List<Predicate> predicates = query(root, cb);
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(formOrder(cb, root));

        fetch(cb, root);

        return em.createQuery(query);
    }

    private Order formOrder(CriteriaBuilder cb, Root<ENTITY> root) {
        Order order;

        if (StringUtils.isNotBlank(criteria.getOrderBy())) {
            order = Objects.requireNonNullElse(criteria.getDescendingOrder(), false)
                    ? cb.desc(root.get(criteria.getOrderBy()))
                    : cb.asc(root.get(criteria.getOrderBy()));
        } else {
            order = cb.asc(root.get("id"));
        }

        return order;
    }

    protected void fetch(CriteriaBuilder cb, Root<ENTITY> root) {}

    public TypedQuery<Long> createCountQuery(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<ENTITY> root = query.from(entityClass);
        query.select(cb.count(root));
        query.distinct(true);
        List<Predicate> predicates = query(root, cb);
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(query);
    }

    public Integer getSize() {
        return criteria.getSize();
    }

    public Integer getPage() {
        return criteria.getPage();
    }
}

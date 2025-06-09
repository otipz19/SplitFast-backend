package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation_;

import java.util.List;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class DebtClosureCriteria extends Criteria<DebtClosureEntity, DebtClosureCriteriaDto> {

    public DebtClosureCriteria(DebtClosureCriteriaDto criteriaDto) {
        super(DebtClosureEntity.class, criteriaDto);
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<DebtClosureEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(criteria.getFromUserId(), root.get(DebtClosureEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.fromUserId))
                .eq(criteria.getToUserId(), root.get(DebtClosureEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.toUserId))
                .between(criteria.getMinAmount(), criteria.getMaxAmount(), DebtClosureEntity_.amount)
                .between(mapToUtcDateTime(criteria.getMinTimeCreated()), mapToUtcDateTime(criteria.getMaxTimeCreated()), DebtClosureEntity_.timeCreated)
                .getPredicates();
    }
}
package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation_;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;

import java.util.List;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class DebtRepaymentRequestCriteria extends Criteria<DebtRepaymentRequestEntity, DebtRepaymentRequestCriteriaDto> {

    private final EnumsMapper enumsMapper;

    public DebtRepaymentRequestCriteria(DebtRepaymentRequestCriteriaDto criteriaDto, EnumsMapper enumsMapper) {
        super(DebtRepaymentRequestEntity.class, criteriaDto);
        this.enumsMapper = enumsMapper;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<DebtRepaymentRequestEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(criteria.getFromUserId(), root.get(DebtRepaymentRequestEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.fromUserId))
                .eq(criteria.getToUserId(), root.get(DebtRepaymentRequestEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.toUserId))
                .between(criteria.getMinAmount(), criteria.getMaxAmount(), DebtRepaymentRequestEntity_.amount)
                .eq(enumsMapper.map(criteria.getStatus()), DebtRepaymentRequestEntity_.status)
                .between(mapToUtcDateTime(criteria.getMinTimeCreated()), mapToUtcDateTime(criteria.getMaxTimeCreated()), DebtRepaymentRequestEntity_.timeCreated)
                .between(mapToUtcDateTime(criteria.getMinTimeUpdated()), mapToUtcDateTime(criteria.getMaxTimeUpdated()), DebtRepaymentRequestEntity_.timeUpdated)
                .getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<DebtRepaymentRequestEntity> root) {
        root.fetch(DebtRepaymentRequestEntity_.usersAssociation).fetch(TwoUsersDirectedAssociation_.fromUser);
        root.fetch(DebtRepaymentRequestEntity_.usersAssociation).fetch(TwoUsersDirectedAssociation_.toUser);
    }
}
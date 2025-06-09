package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.*;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseParticipationCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseParticipationTypeDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType;

import java.math.BigDecimal;
import java.util.List;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class ExpenseCriteria extends Criteria<ExpenseEntity, ExpenseCriteriaDto> {

    private final int activityId;

    public ExpenseCriteria(ExpenseCriteriaDto criteriaDto, int activityId) {
        super(ExpenseEntity.class, criteriaDto);
        this.activityId = activityId;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ExpenseEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        PredicatesBuilder<ExpenseEntity> builder = new PredicatesBuilder<>(root, cb)
                .eq(activityId, ExpenseEntity_.activityId)
                .like(criteria.getName(), ExpenseEntity_.name)
                .between(mapToUtcDateTime(criteria.getMinTimeCreated()), mapToUtcDateTime(criteria.getMaxTimeCreated()), ExpenseEntity_.timeCreated)
                .between(mapToUtcDateTime(criteria.getMinTimeFinished()), mapToUtcDateTime(criteria.getMaxTimeFinished()), ExpenseEntity_.timeFinished);

        addExpenseParticipationPredicates(root, cb, builder);
        addCostPredicate(root, query, cb, builder);
        addIsFinishedPredicate(builder);

        return builder.getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<ExpenseEntity> root) {
        root.fetch(ExpenseEntity_.owner);
    }

    private void addExpenseParticipationPredicates(Root<ExpenseEntity> root, CriteriaBuilder cb, PredicatesBuilder<ExpenseEntity> builder) {
        if (criteria.getExpenseParticipation() == null) return;

        ExpenseParticipationCriteriaDto subCriteria = criteria.getExpenseParticipation();
        Join<ExpenseEntity, ExpenseMemberEntity> join = null;

        for (ExpenseParticipationTypeDto type : subCriteria.getTypes()) {
            switch (type) {
                case OWNER:
                    builder.eq(subCriteria.getUserId(), ExpenseEntity_.ownerId);
                    break;
                case SHAREHOLDER:
                    if (join == null)
                        join = root.join(ExpenseEntity_.members);
                    builder.eq(subCriteria.getUserId(), join.get(ExpenseMemberEntity_.userId))
                            .eq(ExpenseMemberType.SHAREHOLDER, join.get(ExpenseMemberEntity_.type));
                    break;
                case BENEFICIARY:
                    if (join == null)
                        join = root.join(ExpenseEntity_.members);
                    builder.eq(subCriteria.getUserId(), join.get(ExpenseMemberEntity_.userId))
                            .eq(ExpenseMemberType.BENEFICIARY, join.get(ExpenseMemberEntity_.type));
                    break;
                case ANY:
                    if (join == null)
                        join = root.join(ExpenseEntity_.members, JoinType.LEFT);
                    builder.or(
                            cb.equal(join.get(ExpenseMemberEntity_.userId), subCriteria.getUserId()),
                            cb.equal(root.get(ExpenseEntity_.ownerId), subCriteria.getUserId())
                    );
                    break;
            }
        }
    }


    private <R> void addCostPredicate(Root<ExpenseEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb, PredicatesBuilder<ExpenseEntity> builder) {
        if (criteria.getMinCost() == null && criteria.getMaxCost() == null) return;

        Subquery<BigDecimal> subQuery = query.subquery(BigDecimal.class);
        Root<ExpenseMemberEntity> subRoot = subQuery.from(ExpenseMemberEntity.class);
        subQuery
                .select(cb.sum(subRoot.get(ExpenseMemberEntity_.share)))
                .where(
                        cb.equal(subRoot.get(ExpenseMemberEntity_.expenseId), root.get(ExpenseEntity_.id)),
                        cb.equal(subRoot.get(ExpenseMemberEntity_.type), ExpenseMemberType.SHAREHOLDER)
                );
        builder.between(criteria.getMinCost(), criteria.getMaxCost(), cb.coalesce(subQuery, BigDecimal.ZERO));
    }

    private void addIsFinishedPredicate(PredicatesBuilder<ExpenseEntity> builder) {
        if (criteria.getIsFinished() == null) return;

        if (criteria.getIsFinished())
            builder.isNotNull(ExpenseEntity_.timeFinished);
        else
            builder.isNull(ExpenseEntity_.timeFinished);
    }
}

package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity_;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;

import java.util.List;

public class ExpenseMemberCriteria extends Criteria<ExpenseMemberEntity, ExpenseMemberCriteriaDto> {

    private final int expenseId;
    private final EnumsMapper enumsMapper;

    public ExpenseMemberCriteria(ExpenseMemberCriteriaDto criteriaDto, int expenseId, EnumsMapper enumsMapper) {
        super(ExpenseMemberEntity.class, criteriaDto);
        this.expenseId = expenseId;
        this.enumsMapper = enumsMapper;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ExpenseMemberEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(expenseId, ExpenseMemberEntity_.expenseId)
                .in(criteria.getUserIds(), ExpenseMemberEntity_.userId)
                .eq(enumsMapper.map(criteria.getType()), ExpenseMemberEntity_.type)
                .between(criteria.getMinShare(), criteria.getMaxShare(), ExpenseMemberEntity_.share)
                .getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<ExpenseMemberEntity> root) {
        root.fetch(ExpenseMemberEntity_.user);
    }

}

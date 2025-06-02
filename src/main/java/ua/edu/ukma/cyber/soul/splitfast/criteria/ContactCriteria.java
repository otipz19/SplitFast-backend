package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtInfoCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation_;

import java.math.BigDecimal;
import java.util.List;

public class ContactCriteria extends Criteria<ContactEntity, ContactCriteriaDto> {

    public ContactCriteria(ContactCriteriaDto criteriaDto) {
        super(ContactEntity.class, criteriaDto);
    }

    @Override
    public List<Predicate> query(Root<ContactEntity> root, CriteriaBuilder cb) {
        return List.of(cb.or(queryIfThisUserIsFirst(root, cb), queryIfThisUserIsSecond(root, cb)));
    }

    private Predicate queryIfThisUserIsFirst(Root<ContactEntity> root, CriteriaBuilder cb) {
        PredicatesBuilder<ContactEntity> builder = new PredicatesBuilder<>(root, cb)
                .eq(criteria.getThisUserId(), root.get(ContactEntity_.usersAssociation).get(TwoUsersAssociation_.firstUserId))
                .eq(criteria.getOtherUserId(), root.get(ContactEntity_.usersAssociation).get(TwoUsersAssociation_.secondUserId));
        addDebtPredicates(
                builder, criteria.getThisUserDebt(),
                ContactEntity_.firstCurrentDebt, ContactEntity_.firstHistoricalDebt
        );
        addDebtPredicates(
                builder, criteria.getOtherUserDebt(),
                ContactEntity_.secondCurrentDebt, ContactEntity_.secondHistoricalDebt
        );
        return cb.and(builder.getPredicates().toArray(new Predicate[0]));
    }

    private Predicate queryIfThisUserIsSecond(Root<ContactEntity> root, CriteriaBuilder cb) {
        PredicatesBuilder<ContactEntity> builder = new PredicatesBuilder<>(root, cb)
                .eq(criteria.getThisUserId(), root.get(ContactEntity_.usersAssociation).get(TwoUsersAssociation_.secondUserId))
                .eq(criteria.getOtherUserId(), root.get(ContactEntity_.usersAssociation).get(TwoUsersAssociation_.firstUserId));
        addDebtPredicates(
                builder, criteria.getThisUserDebt(),
                ContactEntity_.secondCurrentDebt, ContactEntity_.secondHistoricalDebt
        );
        addDebtPredicates(
                builder, criteria.getOtherUserDebt(),
                ContactEntity_.firstCurrentDebt, ContactEntity_.firstHistoricalDebt
        );
        return cb.and(builder.getPredicates().toArray(new Predicate[0]));
    }

    private void addDebtPredicates(
            PredicatesBuilder<ContactEntity> builder, DebtInfoCriteriaDto debtCriteria,
            SingularAttribute<ContactEntity, BigDecimal> currentDebt, SingularAttribute<ContactEntity, BigDecimal> historicalDebt
    ) {
        if (debtCriteria == null) return;
        builder.between(debtCriteria.getMinCurrent(), debtCriteria.getMaxCurrent(), currentDebt);
        builder.between(debtCriteria.getMinHistorical(),debtCriteria.getMaxHistorical(), historicalDebt);
    }
}

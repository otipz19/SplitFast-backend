package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation_;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;

import java.util.List;

public class ContactRequestCriteria extends Criteria<ContactRequestEntity, ContactRequestCriteriaDto> {

    public ContactRequestCriteria(ContactRequestCriteriaDto criteriaDto) {
        super(ContactRequestEntity.class, criteriaDto);
    }

    @Override
    public List<Predicate> query(Root<ContactRequestEntity> root, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(criteria.getFromUserId(), root.get(ContactRequestEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.fromUserId))
                .eq(criteria.getToUserId(), root.get(ContactRequestEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.toUserId))
                .between(TimeUtils.mapToUtcDateTime(criteria.getMinTimeCreated()), TimeUtils.mapToUtcDateTime(criteria.getMaxTimeCreated()), ContactRequestEntity_.timeCreated)
                .getPredicates();
    }

}

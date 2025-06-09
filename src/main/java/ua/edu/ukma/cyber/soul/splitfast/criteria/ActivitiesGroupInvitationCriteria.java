package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation_;

import java.util.List;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class ActivitiesGroupInvitationCriteria extends Criteria<ActivitiesGroupInvitationEntity, ActivitiesGroupInvitationCriteriaDto> {

    public ActivitiesGroupInvitationCriteria(ActivitiesGroupInvitationCriteriaDto criteriaDto) {
        super(ActivitiesGroupInvitationEntity.class, criteriaDto);
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ActivitiesGroupInvitationEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(criteria.getFromUserId(), root.get(ActivitiesGroupInvitationEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.fromUserId))
                .eq(criteria.getToUserId(), root.get(ActivitiesGroupInvitationEntity_.usersAssociation).get(TwoUsersDirectedAssociation_.toUserId))
                .eq(criteria.getActivitiesGroupId(), ActivitiesGroupInvitationEntity_.activitiesGroupId)
                .between(mapToUtcDateTime(criteria.getMinTimeCreated()), mapToUtcDateTime(criteria.getMaxTimeCreated()), ActivitiesGroupInvitationEntity_.timeCreated)
                .getPredicates();
    }
}

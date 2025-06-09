package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity_;

import java.util.List;

public class ActivitiesGroupMemberCriteria extends Criteria<ActivitiesGroupMemberEntity, ActivitiesGroupMemberCriteriaDto> {

    private final int groupId;

    public ActivitiesGroupMemberCriteria(ActivitiesGroupMemberCriteriaDto criteriaDto, int groupId) {
        super(ActivitiesGroupMemberEntity.class, criteriaDto);
        this.groupId = groupId;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ActivitiesGroupMemberEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(groupId, ActivitiesGroupMemberEntity_.activitiesGroupId)
                .in(criteria.getUserIds(), ActivitiesGroupMemberEntity_.userId)
                .likeJoin(criteria.getQuery(), ActivitiesGroupMemberEntity_.user,
                        UserEntity_.username,
                        UserEntity_.name,
                        UserEntity_.email,
                        UserEntity_.phone
                )
                .eq(criteria.getIsOwner(), ActivitiesGroupMemberEntity_.isOwner)
                .getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<ActivitiesGroupMemberEntity> root) {
        root.fetch(ActivitiesGroupMemberEntity_.user);
    }

}

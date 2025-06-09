package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity_;

import java.util.List;

public class ActivityMemberCriteria extends Criteria<ActivityMemberEntity, ActivityMemberCriteriaDto> {

    private final int activityId;

    public ActivityMemberCriteria(ActivityMemberCriteriaDto criteriaDto, int activityId) {
        super(ActivityMemberEntity.class, criteriaDto);
        this.activityId = activityId;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ActivityMemberEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(activityId, ActivityMemberEntity_.activityId)
                .in(criteria.getUserIds(), ActivityMemberEntity_.userId)
                .likeJoin(criteria.getQuery(), ActivityMemberEntity_.user,
                        UserEntity_.username,
                        UserEntity_.name,
                        UserEntity_.email,
                        UserEntity_.phone
                )
                .eq(criteria.getIsOwner(), ActivityMemberEntity_.isOwner)
                .getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<ActivityMemberEntity> root) {
        root.fetch(ActivityMemberEntity_.user);
    }

}

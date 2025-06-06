package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity_;

import java.util.List;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class ActivityCriteria extends Criteria<ActivityEntity, ActivityCriteriaDto> {

    private final int groupId;

    public ActivityCriteria(ActivityCriteriaDto criteriaDto, int groupId) {
        super(ActivityEntity.class, criteriaDto);
        this.groupId = groupId;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<ActivityEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        PredicatesBuilder<ActivityEntity> builder = new PredicatesBuilder<>(root, cb)
                .eq(groupId, ActivityEntity_.activitiesGroupId)
                .eq(criteria.getUserId(), ActivityEntity_.members, ActivityMemberEntity_.userId)
                .like(criteria.getName(), ActivityEntity_.name)
                .between(mapToUtcDateTime(criteria.getMinTimeCreated()), mapToUtcDateTime(criteria.getMaxTimeCreated()), ActivityEntity_.timeCreated)
                .between(mapToUtcDateTime(criteria.getMinTimeFinished()), mapToUtcDateTime(criteria.getMaxTimeFinished()), ActivityEntity_.timeFinished);
        if (criteria.getIsFinished() != null) {
            if (criteria.getIsFinished())
                builder.isNotNull(ActivityEntity_.timeFinished);
            else
                builder.isNull(ActivityEntity_.timeFinished);
        }
        return builder.getPredicates();
    }
}

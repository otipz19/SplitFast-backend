package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity_;

import java.util.List;
import java.util.Set;

import static ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils.*;

public class ActivityCriteria extends Criteria<ActivityEntity, ActivityCriteriaDto> {

    private final int groupId;
    private final Set<Integer> ids;

    public ActivityCriteria(ActivityCriteriaDto criteriaDto, int groupId, Set<Integer> ids) {
        super(ActivityEntity.class, criteriaDto);
        this.groupId = groupId;
        this.ids = ids;
    }

    @Override
    public List<Predicate> query(Root<ActivityEntity> root, CriteriaBuilder cb) {
        PredicatesBuilder<ActivityEntity> builder = new PredicatesBuilder<>(root, cb)
                .eq(groupId, ActivityEntity_.activitiesGroupId)
                .in(ids, ActivityEntity_.id)
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

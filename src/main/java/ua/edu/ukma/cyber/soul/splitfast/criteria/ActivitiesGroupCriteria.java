package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity_;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;

import java.util.List;
import java.util.Set;

public class ActivitiesGroupCriteria extends Criteria<ActivitiesGroupEntity, ActivitiesGroupCriteriaDto> {

    private final Set<Integer> forcedIds;

    public ActivitiesGroupCriteria(ActivitiesGroupCriteriaDto criteriaDto, Set<Integer> forcedIds) {
        super(ActivitiesGroupEntity.class, criteriaDto);
        this.forcedIds = forcedIds;
    }

    @Override
    public List<Predicate> query(Root<ActivitiesGroupEntity> root, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .forcedIn(forcedIds, ActivitiesGroupEntity_.id)
                .like(criteria.getName(), ActivitiesGroupEntity_.name)
                .between(TimeUtils.mapToUtcDateTime(criteria.getMinTimeCreated()), TimeUtils.mapToUtcDateTime(criteria.getMaxTimeCreated()), ActivitiesGroupEntity_.timeCreated)
                .getPredicates();
    }
}

package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity_;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;

import java.util.List;

public class ActivitiesGroupCriteria extends Criteria<ActivitiesGroupEntity, ActivitiesGroupCriteriaDto> {

    public ActivitiesGroupCriteria(ActivitiesGroupCriteriaDto criteriaDto) {
        super(ActivitiesGroupEntity.class, criteriaDto);
    }

    @Override
    public List<Predicate> query(Root<ActivitiesGroupEntity> root, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(criteria.getUserId(), ActivitiesGroupEntity_.members, ActivitiesGroupMemberEntity_.userId)
                .like(criteria.getName(), ActivitiesGroupEntity_.name)
                .between(TimeUtils.mapToUtcDateTime(criteria.getMinTimeCreated()), TimeUtils.mapToUtcDateTime(criteria.getMaxTimeCreated()), ActivitiesGroupEntity_.timeCreated)
                .getPredicates();
    }
}

package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity_;

import java.util.List;

public class GeoLabelCriteria extends Criteria<GeoLabelEntity, GeoLabelCriteriaDto> {

    private final int groupId;

    public GeoLabelCriteria(GeoLabelCriteriaDto criteriaDto, int groupId) {
        super(GeoLabelEntity.class, criteriaDto);
        this.groupId = groupId;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<GeoLabelEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .eq(groupId, GeoLabelEntity_.activitiesGroupId)
                .like(criteria.getName(), GeoLabelEntity_.name)
                .eq(criteria.getOwnerId(), GeoLabelEntity_.ownerId)
                .getPredicates();
    }

    @Override
    protected void fetch(CriteriaBuilder cb, Root<GeoLabelEntity> root) {
        root.fetch(GeoLabelEntity_.owner);
    }
}

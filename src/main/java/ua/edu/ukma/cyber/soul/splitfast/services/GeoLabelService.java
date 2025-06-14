package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateGeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.GeoLabelCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.GeoLabelMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.GeoLabelRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;

@Service
public class GeoLabelService extends BaseCRUDService<GeoLabelEntity, UpdateGeoLabelDto, UpdateGeoLabelDto, Integer> {

    private final GeoLabelMapper mapper;
    private final ActivitiesGroupRepository activitiesGroupRepository;
    private final SecurityUtils securityUtils;

    public GeoLabelService(GeoLabelRepository repository, CriteriaRepository criteriaRepository,
                           IValidator<GeoLabelEntity> validator, IMerger<GeoLabelEntity, UpdateGeoLabelDto, UpdateGeoLabelDto> merger,
                           ApplicationEventPublisher eventPublisher, GeoLabelMapper mapper, ActivitiesGroupRepository activitiesGroupRepository,
                           SecurityUtils securityUtils) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, GeoLabelEntity.class, GeoLabelEntity::new);
        this.mapper = mapper;
        this.activitiesGroupRepository = activitiesGroupRepository;
        this.securityUtils = securityUtils;
    }

    @SerializableTransaction
    public int createGeoLabel(int activitiesGroupId, UpdateGeoLabelDto view) {
        GeoLabelEntity entity = entitySupplier.get();
        merger.mergeForCreate(entity, view);
        entity.setActivitiesGroup(
                activitiesGroupRepository.findById(activitiesGroupId)
                        .orElseThrow(() -> new ValidationException("error.geo-label.activities-group.not-exists"))
        );
        entity.setOwner(securityUtils.getCurrentUser());
        validator.validForCreate(entity);
        return repository.save(entity).getId();
    }

    @SerializableTransaction(readOnly = true)
    public GeoLabelDto getResponseById(int geoLabelId) {
        return mapper.toResponse(getById(geoLabelId));
    }

    @SerializableTransaction(readOnly = true)
    public GeoLabelListDto getListResponseByCriteria(int activitiesGroupId, GeoLabelCriteriaDto criteriaDto) {
        GeoLabelCriteria criteria = new GeoLabelCriteria(criteriaDto, activitiesGroupId);
        List<GeoLabelEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @EventListener
    public void clearGeoLabels(DeleteEntityEvent<? extends ActivitiesGroupEntity, Integer> deleteEvent) {
        ((GeoLabelRepository) repository).deleteAllByActivitiesGroupId(deleteEvent.getId());
    }
}

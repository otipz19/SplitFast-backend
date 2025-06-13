package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateGeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.GeoLabelCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.GeoLabelMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.GeoLabelRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.GeoLabelValidator;

import java.util.List;

@Service
public class GeoLabelService extends BaseCRUDService<GeoLabelEntity, UpdateGeoLabelDto, UpdateGeoLabelDto, Integer> {

    private final GeoLabelMapper mapper;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public GeoLabelService(GeoLabelRepository repository, CriteriaRepository criteriaRepository,
                           GeoLabelValidator validator, IMerger<GeoLabelEntity, UpdateGeoLabelDto, UpdateGeoLabelDto> merger,
                           ApplicationEventPublisher eventPublisher, GeoLabelMapper mapper,
                           ActivityRepository activityRepository, UserRepository userRepository) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, GeoLabelEntity.class, GeoLabelEntity::new);
        this.mapper = mapper;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @SerializableTransaction
    public int createGeoLabel(UpdateGeoLabelDto view) {
        GeoLabelEntity entity = entitySupplier.get();
        merger.mergeForCreate(entity, view);

        entity.setActivity(activityRepository.findById(view.getActivityId())
                .orElseThrow(() -> new ValidationException("error.geo-label.activity.not-exists")));
        entity.setActivityId(view.getActivityId());

        entity.setCreator(userRepository.findById(view.getCreatorId())
                .orElseThrow(() -> new ValidationException("error.geo-label.creator.not-exists")));
        entity.setCreatorId(view.getCreatorId());

        validator.validForCreate(entity);
        return repository.save(entity).getId();
    }

    @SerializableTransaction(readOnly = true)
    public GeoLabelDto getResponseById(int geoLabelId) {
        GeoLabelEntity entity = getById(geoLabelId);
        return mapper.toResponse(entity);
    }

    @SerializableTransaction(readOnly = true)
    public GeoLabelListDto getListResponseByCriteria(GeoLabelCriteriaDto criteriaDto) {
        GeoLabelCriteria criteria = new GeoLabelCriteria(criteriaDto);
        List<GeoLabelEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @SerializableTransaction
    public void updateLabel(Integer geoLabelId, UpdateGeoLabelDto view) {
        GeoLabelEntity entity = getByIdWithoutValidation(geoLabelId);
        merger.mergeForUpdate(entity, view);

        if (view.getActivityId() != null && entity.getActivityId() != view.getActivityId()) {
            entity.setActivity(activityRepository.findById(view.getActivityId())
                    .orElseThrow(() -> new ValidationException("error.geo-label.activity.not-exists")));
            entity.setActivityId(view.getActivityId());
        }
        if (view.getCreatorId() != null && entity.getCreatorId() != view.getCreatorId()) {
            entity.setCreator(userRepository.findById(view.getCreatorId())
                    .orElseThrow(() -> new ValidationException("error.geo-label.creator.not-exists")));
            entity.setCreatorId(view.getCreatorId());
        }

        validator.validForUpdate(entity);
        repository.save(entity);
    }

    @SerializableTransaction
    @Override
    public void delete(Integer geoLabelId) {
        GeoLabelEntity entity = getByIdWithoutValidation(geoLabelId);
        validator.validForDelete(entity);
        repository.delete(entity);
    }
}

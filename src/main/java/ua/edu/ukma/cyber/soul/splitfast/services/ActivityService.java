package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivityCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.events.FinishEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivityMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ActivityValidator;

import java.util.List;

@Service
public class ActivityService extends BaseCRUDService<ActivityEntity, UpdateActivityDto, UpdateActivityDto, Integer> {

    private final ActivityMapper mapper;
    private final ActivityMemberService memberService;
    private final ActivitiesGroupRepository activitiesGroupRepository;

    public ActivityService(ActivityRepository repository, CriteriaRepository criteriaRepository,
                           ActivityValidator validator, IMerger<ActivityEntity, UpdateActivityDto, UpdateActivityDto> merger,
                           ApplicationEventPublisher eventPublisher, ActivityMapper mapper,
                           ActivityMemberService memberService, ActivitiesGroupRepository activitiesGroupRepository) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, ActivityEntity.class, ActivityEntity::new);
        this.mapper = mapper;
        this.memberService = memberService;
        this.activitiesGroupRepository = activitiesGroupRepository;
    }

    @SerializableTransaction
    public int createActivity(int activitiesGroupId, UpdateActivityDto view) {
        ActivityEntity entity = entitySupplier.get();
        merger.mergeForCreate(entity, view);
        entity.setActivitiesGroup(
                activitiesGroupRepository.findById(activitiesGroupId)
                        .orElseThrow(() -> new ValidationException("error.activity.activities-group.not-exists"))
        );
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
        validator.validForCreate(entity);
        entity = repository.save(entity);
        memberService.createOwnerFor(entity);
        return entity.getId();
    }

    @SerializableTransaction
    public void finishActivity(int activityId) {
        ActivityEntity activity = getByIdWithoutValidation(activityId);
        ((ActivityValidator) validator).validForFinish(activity);
        activity.setTimeFinished(TimeUtils.getCurrentDateTimeUTC());
        eventPublisher.publishEvent(new FinishEntityEvent<>(activity));
        repository.save(activity);
    }

    @SerializableTransaction(readOnly = true)
    public ActivityDto getResponseById(int userId) {
        return mapper.toResponse(getById(userId));
    }

    @SerializableTransaction(readOnly = true)
    public ActivityListDto getListResponseByCriteria(int activitiesGroupId, ActivityCriteriaDto criteriaDto) {
        ActivityCriteria criteria = new ActivityCriteria(criteriaDto, activitiesGroupId);
        List<ActivityEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }
}

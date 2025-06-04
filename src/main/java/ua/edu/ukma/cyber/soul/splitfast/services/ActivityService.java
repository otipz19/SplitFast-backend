package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivityCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivityMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;
import java.util.Set;

@Service
public class ActivityService extends BaseCRUDService<ActivityEntity, UpdateActivityDto, Integer> {

    private final ActivityMapper mapper;
    private final ActivityMemberService memberService;
    private final ActivitiesGroupRepository activitiesGroupRepository;

    public ActivityService(ActivityRepository repository, CriteriaRepository criteriaRepository,
                           IValidator<ActivityEntity> validator, IMerger<ActivityEntity, UpdateActivityDto> merger,
                           ActivityMapper mapper, ActivityMemberService memberService, ActivitiesGroupRepository activitiesGroupRepository) {
        super(repository, criteriaRepository, validator, merger, ActivityEntity.class, ActivityEntity::new);
        this.mapper = mapper;
        this.memberService = memberService;
        this.activitiesGroupRepository = activitiesGroupRepository;
    }

    @Transactional
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

    @Transactional
    public void finishActivity(int activityId) {
        ActivityEntity activity = getByIdWithoutValidation(activityId);
        validator.validForUpdate(activity);
        activity.setTimeFinished(TimeUtils.getCurrentDateTimeUTC());
        // TODO: finish all spending within this activity
        repository.save(activity);
    }

    @Transactional(readOnly = true)
    public ActivityDto getResponseById(int userId) {
        return mapper.toResponse(getById(userId));
    }

    @Transactional(readOnly = true)
    public ActivityListDto getListResponseByCriteria(int activitiesGroupId, ActivityCriteriaDto criteriaDto) {
        ActivityCriteria criteria = new ActivityCriteria(criteriaDto, activitiesGroupId, getIds(criteriaDto));
        List<ActivityEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    private Set<Integer> getIds(ActivityCriteriaDto criteriaDto) {
        if (criteriaDto.getUserId() != null)
            return memberService.getActivityIdsWhereUserIsMember(criteriaDto.getUserId());
        return null;
    }
}

package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivityMemberCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivityMemberMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ActivityMemberValidator;

import java.util.List;

@Service
public class ActivityMemberService {

    private final ActivityMemberMapper mapper;
    private final ActivityMemberRepository repository;
    private final ActivityMemberValidator validator;
    private final CriteriaRepository criteriaRepository;
    private final ActivityService activityService;
    private final SecurityUtils securityUtils;

    public ActivityMemberService(ActivityMemberMapper mapper, ActivityMemberRepository repository,
                                 ActivityMemberValidator validator, CriteriaRepository criteriaRepository,
                                 @Lazy ActivityService activityService, SecurityUtils securityUtils) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
        this.criteriaRepository = criteriaRepository;
        this.activityService = activityService;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public ActivityMemberListDto getListResponseByCriteria(int activityId, ActivityMemberCriteriaDto criteriaDto) {
        activityService.getById(activityId); // validate activity exists and current user has access

        ActivityMemberCriteria criteria = new ActivityMemberCriteria(criteriaDto, activityId);
        List<ActivityMemberEntity> members = criteriaRepository.find(criteria);
        long total = criteriaRepository.count(criteria);
        return mapper.toListResponse(total, members);
    }

    @Transactional
    public void joinActivity(int activityId) {
        ActivityEntity activity = activityService.getByIdWithoutValidation(activityId);
        validator.validForJoin(activity);
        create(activity, false);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createOwnerFor(ActivityEntity activity) {
        create(activity, true);
    }

    private void create(ActivityEntity activity, boolean isOwner) {
        ActivityMemberEntity member = new ActivityMemberEntity();
        member.setActivity(activity);
        member.setUser(securityUtils.getCurrentUser());
        member.setOwner(isOwner);
        repository.save(member);
    }

    @EventListener
    public void clearActivityMembers(DeleteEntityEvent<ActivityEntity, Integer> deleteEvent) {
        repository.deleteAllByActivityId(deleteEvent.getId());
    }
}

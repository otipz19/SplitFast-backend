package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivitiesGroupCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivitiesGroupMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;

@Service
public class ActivitiesGroupService extends BaseCRUDService<ActivitiesGroupEntity, UpdateActivitiesGroupDto, Integer> {

    private final ActivitiesGroupMapper mapper;
    private final ActivitiesGroupMemberService memberService;
    private final SecurityUtils securityUtils;

    public ActivitiesGroupService(ActivitiesGroupRepository repository, CriteriaRepository criteriaRepository,
                                  IValidator<ActivitiesGroupEntity> validator, IMerger<ActivitiesGroupEntity, UpdateActivitiesGroupDto> merger,
                                  ApplicationEventPublisher eventPublisher, ActivitiesGroupMapper mapper,
                                  ActivitiesGroupMemberService memberService, SecurityUtils securityUtils) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, ActivitiesGroupEntity.class, ActivitiesGroupEntity::new);
        this.mapper = mapper;
        this.memberService = memberService;
        this.securityUtils = securityUtils;
    }

    @Transactional
    @Override
    public ActivitiesGroupEntity createEntity(@NonNull UpdateActivitiesGroupDto view) {
        ActivitiesGroupEntity group = super.createEntity(view);
        memberService.createOwnerFor(group);
        return group;
    }

    @Override
    protected void postCreate(@NonNull ActivitiesGroupEntity entity, @NonNull UpdateActivitiesGroupDto view) {
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
    }

    @Transactional(readOnly = true)
    public ActivitiesGroupDto getResponseById(int userId) {
        return mapper.toResponse(getById(userId));
    }

    @Transactional(readOnly = true)
    public ActivitiesGroupListDto getListResponseByCriteria(ActivitiesGroupCriteriaDto criteriaDto) {
        forceUserId(criteriaDto);
        ActivitiesGroupCriteria criteria = new ActivitiesGroupCriteria(criteriaDto);
        List<ActivitiesGroupEntity> entities = criteriaRepository.find(criteria); // skip validation because of forced user id
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    private void forceUserId(ActivitiesGroupCriteriaDto criteriaDto) {
        if (securityUtils.hasRole(UserRole.USER))
            criteriaDto.setUserId(securityUtils.getCurrentUserId());
    }
}

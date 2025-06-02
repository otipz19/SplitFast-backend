package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivitiesGroupCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivitiesGroupMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;
import java.util.Set;

@Service
public class ActivitiesGroupService extends BaseCRUDService<ActivitiesGroupEntity, UpdateActivitiesGroupDto, Integer> {

    private final ActivitiesGroupMapper mapper;
    private final ActivitiesGroupMemberRepository memberRepository;
    private final SecurityUtils securityUtils;

    public ActivitiesGroupService(IRepository<ActivitiesGroupEntity, Integer> repository, CriteriaRepository criteriaRepository,
                                  IValidator<ActivitiesGroupEntity> validator, IMerger<ActivitiesGroupEntity, UpdateActivitiesGroupDto> merger,
                                  ActivitiesGroupMapper mapper, ActivitiesGroupMemberRepository memberRepository, SecurityUtils securityUtils) {
        super(repository, criteriaRepository, validator, merger, ActivitiesGroupEntity.class, ActivitiesGroupEntity::new);
        this.mapper = mapper;
        this.memberRepository = memberRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    @Override
    public ActivitiesGroupEntity createEntity(@NonNull UpdateActivitiesGroupDto view) {
        ActivitiesGroupEntity entity = super.createEntity(view);
        createOwner(entity);
        return entity;
    }

    private void createOwner(ActivitiesGroupEntity entity) {
        ActivitiesGroupMemberEntity owner = new ActivitiesGroupMemberEntity();
        owner.setActivitiesGroup(entity);
        owner.setUser(securityUtils.getCurrentUser());
        owner.setOwner(true);
        memberRepository.save(owner);
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
        ActivitiesGroupCriteria criteria = new ActivitiesGroupCriteria(criteriaDto, getForcedIds(criteriaDto));
        List<ActivitiesGroupEntity> entities = criteriaRepository.find(criteria); // skip validation because of forcedIds
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    private Set<Integer> getForcedIds(ActivitiesGroupCriteriaDto criteriaDto) {
        if (securityUtils.hasRole(UserRole.USER))
            return memberRepository.findActivitiesGroupIdsByUserId(securityUtils.getCurrentUser().getId());
        else if (criteriaDto.getUserId() != null)
            return memberRepository.findActivitiesGroupIdsByUserId(criteriaDto.getUserId());
        return null;
    }
}

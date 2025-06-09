package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivitiesGroupMemberCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivitiesGroupMemberMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ActivitiesGroupMemberValidator;

import java.util.List;

@Service
public class ActivitiesGroupMemberService {

    private final ActivitiesGroupMemberMapper mapper;
    private final ActivitiesGroupMemberRepository repository;
    private final CriteriaRepository criteriaRepository;
    private final ActivitiesGroupMemberValidator validator;
    private final ActivitiesGroupService activitiesGroupService;
    private final SecurityUtils securityUtils;

    public ActivitiesGroupMemberService(ActivitiesGroupMemberMapper mapper, ActivitiesGroupMemberRepository repository,
                                        CriteriaRepository criteriaRepository, ActivitiesGroupMemberValidator validator,
                                        @Lazy ActivitiesGroupService activitiesGroupService, SecurityUtils securityUtils) {
        this.mapper = mapper;
        this.repository = repository;
        this.criteriaRepository = criteriaRepository;
        this.validator = validator;
        this.activitiesGroupService = activitiesGroupService;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public ActivitiesGroupMemberListDto getListResponseByCriteria(int groupId, ActivitiesGroupMemberCriteriaDto criteriaDto) {
        activitiesGroupService.getById(groupId); // validate group exists and current user has access

        ActivitiesGroupMemberCriteria criteria = new ActivitiesGroupMemberCriteria(criteriaDto, groupId);
        List<ActivitiesGroupMemberEntity> members = criteriaRepository.find(criteria);
        long total = criteriaRepository.count(criteria);
        return mapper.toListResponse(total, members);
    }

    @Transactional
    public void endActivitiesGroupMembership(int groupId, int userId) {
        ActivitiesGroupMemberEntity membership = repository.findByUserIdAndActivitiesGroupId(userId, groupId)
                .orElseThrow(() -> new NotFoundException(ActivitiesGroupMemberEntity.class, "groupId: " + groupId + ", userId: " + userId));
        validator.validForEnd(membership);
        repository.delete(membership);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createOwnerFor(ActivitiesGroupEntity group) {
        ActivitiesGroupMemberEntity owner = new ActivitiesGroupMemberEntity();
        owner.setActivitiesGroup(group);
        owner.setUser(securityUtils.getCurrentUser());
        owner.setOwner(true);
        repository.save(owner);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createMemberFrom(ActivitiesGroupInvitationEntity invitation) {
        ActivitiesGroupMemberEntity member = new ActivitiesGroupMemberEntity();
        member.setActivitiesGroup(invitation.getActivitiesGroup());
        member.setUser(invitation.getUsersAssociation().getToUser());
        member.setOwner(false);
        repository.save(member);
    }

    @EventListener
    public void clearActivitiesGroupMembers(DeleteEntityEvent<ActivitiesGroupEntity, Integer> deleteEvent) {
        repository.deleteAllByActivitiesGroupId(deleteEvent.getId());
    }
}

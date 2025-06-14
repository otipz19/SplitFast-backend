package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateActivitiesGroupInvitationDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ActivitiesGroupInvitationCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ActivitiesGroupInvitationMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupInvitationRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ActivitiesGroupInvitationValidator;

import java.util.List;

@Service
public class ActivitiesGroupInvitationService extends BaseCRUDService<ActivitiesGroupInvitationEntity, CreateActivitiesGroupInvitationDto, Void, Integer> {

    private final ActivitiesGroupMemberService memberService;
    private final ActivitiesGroupInvitationMapper mapper;
    private final SecurityUtils securityUtils;

    public ActivitiesGroupInvitationService(ActivitiesGroupInvitationRepository repository, CriteriaRepository criteriaRepository,
                                            ActivitiesGroupInvitationValidator validator, IMerger<ActivitiesGroupInvitationEntity, CreateActivitiesGroupInvitationDto, Void> merger,
                                            ApplicationEventPublisher eventPublisher, ActivitiesGroupMemberService memberService,
                                            ActivitiesGroupInvitationMapper mapper, SecurityUtils securityUtils)
    {
        super(repository, criteriaRepository, validator, merger, eventPublisher, ActivitiesGroupInvitationEntity.class, ActivitiesGroupInvitationEntity::new);
        this.memberService = memberService;
        this.mapper = mapper;
        this.securityUtils = securityUtils;
    }

    @SerializableTransaction(readOnly = true)
    public ActivitiesGroupInvitationListDto getListResponseByCriteria(ActivitiesGroupInvitationCriteriaDto criteriaDto) {
        ActivitiesGroupInvitationCriteria criteria = new ActivitiesGroupInvitationCriteria(criteriaDto);
        List<ActivitiesGroupInvitationEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @Override
    protected void postCreate(@NonNull ActivitiesGroupInvitationEntity entity, @NonNull CreateActivitiesGroupInvitationDto view) {
        UserEntity user = securityUtils.getCurrentUser();
        TwoUsersDirectedAssociation association = entity.getUsersAssociation();
        association.setFromUser(user);
        association.setFromUserId(user.getId());
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
    }

    @SerializableTransaction
    public void acceptInvitation(int invitationId) {
        ActivitiesGroupInvitationEntity invitation = getByIdWithoutValidation(invitationId);
        ((ActivitiesGroupInvitationValidator) validator).validForAccept(invitation);
        memberService.createMemberFrom(invitation);
        repository.delete(invitation);
    }

    @EventListener
    public void clearActivitiesGroupInvitations(DeleteEntityEvent<? extends ActivitiesGroupEntity, Integer> deleteEvent) {
        ((ActivitiesGroupInvitationRepository) repository).deleteAllByActivitiesGroupId(deleteEvent.getId());
    }
}

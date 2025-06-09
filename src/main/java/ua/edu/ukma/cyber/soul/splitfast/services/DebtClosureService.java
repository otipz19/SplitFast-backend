package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.DebtClosureCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.mappers.DebtClosureMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.*;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;

@Service
public class DebtClosureService extends BaseCRUDService<DebtClosureEntity, CreateDebtClosureDto, Void, Integer> {

    private final DebtClosureMapper mapper;
    private final SecurityUtils securityUtils;
    private final ContactService contactService;

    public DebtClosureService(IRepository<DebtClosureEntity, Integer> repository, CriteriaRepository criteriaRepository,
                              IValidator<DebtClosureEntity> validator, IMerger<DebtClosureEntity, CreateDebtClosureDto, Void> merger,
                              ApplicationEventPublisher eventPublisher, DebtClosureMapper mapper,
                              SecurityUtils securityUtils, ContactService contactService) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, DebtClosureEntity.class, DebtClosureEntity::new);
        this.mapper = mapper;
        this.securityUtils = securityUtils;
        this.contactService = contactService;
    }

    @Transactional
    @Override
    public DebtClosureEntity createEntity(@NonNull CreateDebtClosureDto createDto) {
        DebtClosureEntity entity = super.createEntity(createDto);
        contactService.updateContact(entity.getUsersAssociation(), entity.getAmount());
        return entity;
    }

    @Override
    protected void postCreate(@NonNull DebtClosureEntity entity, @NonNull CreateDebtClosureDto view) {
        UserEntity user = securityUtils.getCurrentUser();
        TwoUsersDirectedAssociation association = entity.getUsersAssociation();
        association.setToUser(user);
        association.setToUserId(user.getId());
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
    }

    @Transactional(readOnly = true)
    public DebtClosureListDto getListResponseByCriteria(DebtClosureCriteriaDto criteriaDto) {
        DebtClosureCriteria criteria = new DebtClosureCriteria(criteriaDto);
        List<DebtClosureEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

}
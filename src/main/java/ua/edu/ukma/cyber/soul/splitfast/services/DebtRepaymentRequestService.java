package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.DebtRepaymentRequestCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.DebtRepaymentRequestStatus;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

import ua.edu.ukma.cyber.soul.splitfast.mappers.DebtRepaymentRequestMapper;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;

@Service
public class DebtRepaymentRequestService extends BaseCRUDService<DebtRepaymentRequestEntity, CreateDebtRepaymentRequestDto, Void, Integer> {

    private final DebtRepaymentRequestMapper mapper;
    private final EnumsMapper enumsMapper;
    private final SecurityUtils securityUtils;
    private final ContactService contactService;

    public DebtRepaymentRequestService(IRepository<DebtRepaymentRequestEntity, Integer> repository, CriteriaRepository criteriaRepository,
                                       IValidator<DebtRepaymentRequestEntity> validator, IMerger<DebtRepaymentRequestEntity, CreateDebtRepaymentRequestDto, Void> merger,
                                       ApplicationEventPublisher eventPublisher, DebtRepaymentRequestMapper mapper, EnumsMapper enumsMapper,
                                       SecurityUtils securityUtils, ContactService contactService) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, DebtRepaymentRequestEntity.class, DebtRepaymentRequestEntity::new);
        this.mapper = mapper;
        this.enumsMapper = enumsMapper;
        this.securityUtils = securityUtils;
        this.contactService = contactService;
    }

    @Override
    protected void postCreate(@NonNull DebtRepaymentRequestEntity entity, @NonNull CreateDebtRepaymentRequestDto view) {
        UserEntity user = securityUtils.getCurrentUser();
        TwoUsersDirectedAssociation association = entity.getUsersAssociation();
        association.setFromUser(user);
        association.setFromUserId(user.getId());
        entity.setStatus(DebtRepaymentRequestStatus.PENDING);
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
    }

    @SerializableTransaction
    public void acceptDebtRepaymentRequest(Integer id) {
        DebtRepaymentRequestEntity request = getByIdWithoutValidation(id);
        updateStatus(request, DebtRepaymentRequestStatus.ACCEPTED);
        contactService.updateContact(request.getUsersAssociation(), request.getAmount());
    }

    @SerializableTransaction
    public void declineDebtRepaymentRequest(Integer id) {
        DebtRepaymentRequestEntity request = getByIdWithoutValidation(id);
        updateStatus(request, DebtRepaymentRequestStatus.DECLINED);
    }

    private void updateStatus(DebtRepaymentRequestEntity request, DebtRepaymentRequestStatus status) {
        validator.validForUpdate(request);
        request.setStatus(status);
        request.setTimeUpdated(TimeUtils.getCurrentDateTimeUTC());
        repository.save(request);
    }

    @SerializableTransaction(readOnly = true)
    public DebtRepaymentRequestDto getResponseById(int id) {
        return mapper.toResponse(getById(id));
    }

    @SerializableTransaction(readOnly = true)
    public DebtRepaymentRequestListDto getListResponseByCriteria(DebtRepaymentRequestCriteriaDto criteriaDto) {
        DebtRepaymentRequestCriteria criteria = new DebtRepaymentRequestCriteria(criteriaDto, enumsMapper);
        List<DebtRepaymentRequestEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }
}
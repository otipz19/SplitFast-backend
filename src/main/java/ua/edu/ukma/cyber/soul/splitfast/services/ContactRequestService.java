package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ContactRequestCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ContactRequestMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactRequestValidator;

import java.util.List;

@Service
public class ContactRequestService extends BaseCRUDService<ContactRequestEntity, CreateContactRequestDto, Integer> {

    private final ContactService contactService;
    private final SecurityUtils securityUtils;
    private final ContactRequestMapper mapper;

    public ContactRequestService(ContactRequestRepository repository, CriteriaRepository criteriaRepository,
                                 ContactRequestValidator validator, IMerger<ContactRequestEntity, CreateContactRequestDto> merger,
                                 ContactService contactService, SecurityUtils securityUtils, ContactRequestMapper mapper)
    {
        super(repository, criteriaRepository, validator, merger, ContactRequestEntity.class, ContactRequestEntity::new);
        this.contactService = contactService;
        this.securityUtils = securityUtils;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ContactRequestListDto getListResponseByCriteria(ContactRequestCriteriaDto criteriaDto) {
        ContactRequestCriteria criteria = new ContactRequestCriteria(criteriaDto);
        List<ContactRequestEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @Override
    protected void postCreate(@NonNull ContactRequestEntity entity, @NonNull CreateContactRequestDto view) {
        UserEntity user = securityUtils.getCurrentUser();
        TwoUsersDirectedAssociation association = entity.getUsersAssociation();
        association.setFromUser(user);
        association.setFromUserId(user.getId());
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
    }

    @Transactional
    public void acceptContactRequest(int requestId) {
        ContactRequestEntity request = getByIdWithoutValidation(requestId);
        ((ContactRequestValidator) validator).validForAccept(request);
        contactService.createFromRequest(request);
        ((ContactRequestRepository) repository).deleteAllByUsersAssociation(request.getUsersAssociation());
    }
}

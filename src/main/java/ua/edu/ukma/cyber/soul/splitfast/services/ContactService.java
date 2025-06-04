package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ContactCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ContactMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository repository;
    private final CriteriaRepository criteriaRepository;
    private final ContactMapper mapper;
    private final ContactValidator validator;
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    public ContactListDto getListResponseByCriteria(ContactCriteriaDto criteriaDto) {
        ContactCriteria criteria = new ContactCriteria(criteriaDto);
        List<ContactEntity> entities = criteriaRepository.find(criteria);
        validator.validForView(entities);
        long total = criteriaRepository.count(criteria);
        return mapper.toListResponse(criteriaDto.getThisUserId(), total, entities);
    }

    @Transactional
    public void setIsMarked(int contactId, boolean isMarked) {
        ContactEntity contact = repository.findById(contactId)
                .orElseThrow(() -> new NotFoundException(ContactEntity.class, "id: " + contactId));
        int currentUserId = securityUtils.getCurrentUserId();
        if (contact.getUsersAssociation().getFirstUserId() == currentUserId)
            contact.setFirstIsMarked(isMarked);
        else if (contact.getUsersAssociation().getSecondUserId() == currentUserId)
            contact.setSecondIsMarked(isMarked);
        else
            throw new ForbiddenException();
        repository.save(contact);
    }

}

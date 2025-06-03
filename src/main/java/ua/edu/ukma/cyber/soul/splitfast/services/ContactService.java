package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ContactCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ContactMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final CriteriaRepository criteriaRepository;
    private final ContactMapper mapper;
    private final ContactValidator validator;

    @Transactional(readOnly = true)
    public ContactListDto getListResponseByCriteria(ContactCriteriaDto criteriaDto) {
        ContactCriteria criteria = new ContactCriteria(criteriaDto);
        List<ContactEntity> entities = criteriaRepository.find(criteria);
        validator.validForView(entities);
        long total = criteriaRepository.count(criteria);
        return mapper.toListResponse(criteriaDto.getThisUserId(), total, entities);
    }

}

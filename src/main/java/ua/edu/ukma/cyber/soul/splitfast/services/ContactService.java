package ua.edu.ukma.cyber.soul.splitfast.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.ContactId;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ContactRequestMapper;
import ua.edu.ukma.cyber.soul.splitfast.mappers.IMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactRequestValidator;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService extends BaseCRUDService<ContactEntity, ContactDto, UpdateContactDto, ContactId>{

   private final ContactRepository contactRepository;
    private final ContactRequestRepository contactRequestRepository;
    private final ContactRequestMapper contactRequestMapper;
    private final ContactRequestValidator contactRequestValidator;

    public ContactService(IRepository<ContactEntity, ContactId> repository, ContactValidator validator,
                          IMerger<ContactEntity, UpdateContactDto> merger, IMapper<ContactEntity, ContactDto> mapper,
                          ContactRepository contactRepository, ContactRequestRepository contactRequestRepository, ContactRequestMapper contactRequestMapper, ContactRequestValidator contactRequestValidator) {
        super(repository, validator, merger, mapper, ContactEntity.class, ContactEntity::new);
        this.contactRepository = contactRepository;
        this.contactRequestRepository = contactRequestRepository;
        this.contactRequestMapper = contactRequestMapper;
        this.contactRequestValidator = contactRequestValidator;
    }
    public List<ContactDto> getUserContacts(Integer userId) {
        return contactRepository.findByIdFirstUserIdOrIdSecondUserId(userId, userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

   public List<ContactRequestDto> getContactRequests(Integer userId) {
        return contactRequestRepository.findByFromUserIdOrToUserId(userId, userId)
                .stream()
                .map(contactRequestMapper::toResponse)
                .toList();
   }

    @Transactional
    public void sendContactRequest(Integer fromUserId, ContactRequestCreateDto dto) {
        Integer toUserId = dto.getToUserId();

        if (contactRepository.existsByIdFirstUserIdAndIdSecondUserId(fromUserId, toUserId) ||
                contactRepository.existsByIdFirstUserIdAndIdSecondUserId(toUserId, fromUserId)) {
            throw new IllegalStateException("Contact already exists");
        }

        if (contactRequestRepository.findByFromUserIdAndToUserId(fromUserId, toUserId).isPresent()) {
            throw new IllegalStateException("Request already sent");
        }

        ContactRequestEntity entity = new ContactRequestEntity();
        entity.setFromUserId(fromUserId);
        entity.setToUserId(toUserId);
        entity.setTimeCreated(LocalDateTime.now());

        contactRequestValidator.validateForAccept(entity);
        contactRequestRepository.save(entity);
    }

    @Transactional
    public void acceptContactRequest(Integer requestId) {
        ContactRequestEntity request = contactRequestRepository.findById(Long.valueOf(requestId))
                .orElseThrow(() -> new EntityNotFoundException("Contact request not found"));

        Integer userA = request.getFromUserId();
        Integer userB = request.getToUserId();

        ContactId contactId = userA < userB
                ? new ContactId(userA, userB)
                : new ContactId(userB, userA);

        if (contactRepository.existsById(contactId)) {
            contactRequestRepository.delete(request);
            throw new IllegalStateException("Contact already exists");
        }

        ContactEntity contact = new ContactEntity();
        contact.setId(contactId);
        contact.setFirstHistoryDebt(0.0);
        contact.setFirstCurrentDebt(0.0);
        contact.setSecondHistoryDebt(0.0);
        contact.setSecondCurrentDebt(0.0);

        ((ContactValidator) validator).validateForAccept(contact);
        contactRepository.save(contact);
        contactRequestRepository.delete(request);
    }

    @Transactional
    public void declineContactRequest(Integer requestId) {
        ContactRequestEntity request = contactRequestRepository.findById(Long.valueOf(requestId))
                .orElseThrow(() -> new EntityNotFoundException("Contact request not found"));
        contactRequestRepository.delete(request);
    }

    @Transactional
    public ContactDto updateContact(Integer userId, Integer secondUserId, UpdateContactDto updateDto) {
        ContactId contactId = userId < secondUserId
                ? new ContactId(userId, secondUserId)
                : new ContactId(secondUserId, userId);

        ContactEntity contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        merger.mergeForUpdate(contact, updateDto);

        validator.validForUpdate(contact);
        ContactEntity updated = contactRepository.save(contact);

        return mapper.toResponse(updated);
    }


    @Transactional
    public void deleteContact(Integer userId1, Integer userId2) {
        ContactId contactId = userId1 < userId2
                ? new ContactId(userId1, userId2)
                : new ContactId(userId2, userId1);

        ContactEntity contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        contactRepository.delete(contact);
    }

}

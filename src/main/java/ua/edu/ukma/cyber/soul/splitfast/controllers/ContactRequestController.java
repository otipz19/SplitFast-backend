package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ContactRequestControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactRequestService;

@RestController
@RequiredArgsConstructor
public class ContactRequestController implements ContactRequestControllerApi{

    private final ContactRequestService service;

    @Override
    public ResponseEntity<ContactRequestListDto> getContactRequestsByCriteria(ContactRequestCriteriaDto contactRequestCriteriaDto) {
        return ResponseEntity.ok(service.getListResponseByCriteria(contactRequestCriteriaDto));
    }

    @Override
    public ResponseEntity<Void> sendContactRequest(CreateContactRequestDto createContactRequestDto) {
        service.create(createContactRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> acceptContactRequest(Integer requestId) {
        service.acceptContactRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> declineContactRequest(Integer requestId) {
        service.delete(requestId);
        return ResponseEntity.noContent().build();
    }

}

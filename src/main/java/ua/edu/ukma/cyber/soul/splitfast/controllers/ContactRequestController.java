package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ContactRequestControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestCreateDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactService;
import ua.edu.ukma.cyber.soul.splitfast.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactRequestController  implements ContactRequestControllerApi{

    private final ContactService contactService;
    private final UserService service;

    private Integer getCurrentUserId() {
        return service.getCurrentUser().getId();
    }

    @Override
    public ResponseEntity<Void> acceptContactRequest(@PathVariable Integer requestId) {
        contactService.acceptContactRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> declineContactRequest(@PathVariable Integer requestId) {
        contactService.declineContactRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ContactRequestDto>> getContactRequests() {
        Integer userId = getCurrentUserId();
        return ResponseEntity.ok(contactService.getContactRequests(userId));
    }

    @Override
    public ResponseEntity<Void> sendContactRequest(ContactRequestCreateDto contactRequestCreateDto) {
        Integer fromUserId = getCurrentUserId();
        contactService.sendContactRequest(fromUserId, contactRequestCreateDto);
        return ResponseEntity.ok().build();
    }
}

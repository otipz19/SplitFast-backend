package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ContactControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactService;
import ua.edu.ukma.cyber.soul.splitfast.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController implements ContactControllerApi {

    private final ContactService contactService;
    private final UserService service;

    private Integer getCurrentUserId() {
        return service.getCurrentUser().getId();
    }

    @Override
    public ResponseEntity<List<ContactDto>> getUserContacts() {
        Integer userId = getCurrentUserId();
        return ResponseEntity.ok(contactService.getUserContacts(userId));
    }

    @Override
    public ResponseEntity<Void> deleteContact(@PathVariable Integer secondUserId) {
        Integer fromUserId = getCurrentUserId();
        contactService.deleteContact(fromUserId, secondUserId);
        return ResponseEntity.noContent().build();
    }

}

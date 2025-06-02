package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ContactControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactService;

@RestController
@RequiredArgsConstructor
public class ContactController implements ContactControllerApi {

    private final ContactService service;

    @Override
    public ResponseEntity<ContactListDto> getContactsByCriteria(ContactCriteriaDto contactCriteriaDto) {
        return ResponseEntity.ok(service.getListResponseByCriteria(contactCriteriaDto));
    }
}

package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ActivitiesGroupInvitationControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateActivitiesGroupInvitationDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ActivitiesGroupInvitationService;

@RestController
@RequiredArgsConstructor
public class ActivitiesGroupInvitationController implements ActivitiesGroupInvitationControllerApi {

    private final ActivitiesGroupInvitationService service;

    @Override
    public ResponseEntity<ActivitiesGroupInvitationListDto> getActivitiesGroupInvitationsByCriteria(ActivitiesGroupInvitationCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }

    @Override
    public ResponseEntity<Void> sendActivitiesGroupInvitation(CreateActivitiesGroupInvitationDto createActivitiesGroupInvitationDto) {
        service.create(createActivitiesGroupInvitationDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> acceptActivitiesGroupInvitation(Integer invitationId) {
        service.acceptInvitation(invitationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> declineActivitiesGroupInvitation(Integer invitationId) {
        service.delete(invitationId);
        return ResponseEntity.noContent().build();
    }
}

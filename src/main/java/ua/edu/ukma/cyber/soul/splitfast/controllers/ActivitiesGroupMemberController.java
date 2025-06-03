package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ActivitiesGroupMemberControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ActivitiesGroupMemberService;

@RestController
@RequiredArgsConstructor
public class ActivitiesGroupMemberController implements ActivitiesGroupMemberControllerApi {

    private final ActivitiesGroupMemberService service;

    @Override
    public ResponseEntity<ActivitiesGroupMemberListDto> getActivitiesGroupMembersByCriteria(Integer groupId, ActivitiesGroupMemberCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(groupId, criteria));
    }
}

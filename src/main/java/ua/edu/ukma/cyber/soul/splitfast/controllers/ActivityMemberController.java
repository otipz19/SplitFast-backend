package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ActivityMemberControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ActivityMemberService;

@RestController
@RequiredArgsConstructor
public class ActivityMemberController implements ActivityMemberControllerApi {

    private final ActivityMemberService service;

    @Override
    public ResponseEntity<ActivityMemberListDto> getActivityMembersByCriteria(Integer activityId, ActivityMemberCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(activityId, criteria));
    }

    @Override
    public ResponseEntity<Void> joinActivity(Integer activityId) {
        service.joinActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}

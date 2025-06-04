package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ActivityControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateActivityDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ActivityService;

@RestController
@RequiredArgsConstructor
public class ActivityController implements ActivityControllerApi {

    private final ActivityService service;

    @Override
    public ResponseEntity<ActivityListDto> getActivitiesByCriteria(Integer groupId, ActivityCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(groupId, criteria));
    }

    @Override
    public ResponseEntity<ActivityDto> getActivityById(Integer activityId) {
        return ResponseEntity.ok(service.getResponseById(activityId));
    }

    @Override
    public ResponseEntity<Integer> createActivity(Integer groupId, UpdateActivityDto updateActivityDto) {
        return ResponseEntity.ok(service.createActivity(groupId, updateActivityDto));
    }

    @Override
    public ResponseEntity<Void> updateActivity(Integer activityId, UpdateActivityDto updateActivityDto) {
        service.update(activityId, updateActivityDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> finishActivity(Integer activityId) {
        service.finishActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}

package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ActivitiesGroupControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateActivitiesGroupDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ActivitiesGroupService;

@RestController
@RequiredArgsConstructor
public class ActivitiesGroupController implements ActivitiesGroupControllerApi {

    private final ActivitiesGroupService service;

    @Override
    public ResponseEntity<ActivitiesGroupDto> getActivitiesGroupById(Integer groupId) {
        return ResponseEntity.ok(service.getResponseById(groupId));
    }

    @Override
    public ResponseEntity<ActivitiesGroupListDto> getActivitiesGroupsByCriteria(ActivitiesGroupCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }

    @Override
    public ResponseEntity<Integer> createActivitiesGroup(UpdateActivitiesGroupDto updateActivitiesGroupDto) {
        return ResponseEntity.ok(service.create(updateActivitiesGroupDto));
    }

    @Override
    public ResponseEntity<Void> updateActivitiesGroup(Integer groupId, UpdateActivitiesGroupDto updateActivitiesGroupDto) {
        service.update(groupId, updateActivitiesGroupDto);
        return ResponseEntity.noContent().build();
    }
}

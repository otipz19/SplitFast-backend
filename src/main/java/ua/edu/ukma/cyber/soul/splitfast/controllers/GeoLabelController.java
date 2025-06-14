package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.GeoLabelControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateGeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.services.GeoLabelService;


@RestController
@RequiredArgsConstructor
public class GeoLabelController implements GeoLabelControllerApi {

    private final GeoLabelService service;

    @Override
    public ResponseEntity<GeoLabelDto> getGeoLabelById(Integer geoLabelId) {
        return ResponseEntity.ok(service.getResponseById(geoLabelId));
    }

    @Override
    public ResponseEntity<GeoLabelListDto> getGeoLabelsByCriteria(Integer groupId, GeoLabelCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(groupId, criteria));
    }

    @Override
    public ResponseEntity<Integer> createGeoLabel(Integer groupId, UpdateGeoLabelDto updateGeoLabelDto) {
        return ResponseEntity.ok(service.createGeoLabel(groupId, updateGeoLabelDto));
    }

    @Override
    public ResponseEntity<Void> updateGeoLabel(Integer geoLabelId, UpdateGeoLabelDto updateGeoLabelDto) {
        service.update(geoLabelId, updateGeoLabelDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteGeoLabel(Integer geoLabelId) {
        service.delete(geoLabelId);
        return ResponseEntity.noContent().build();
    }
}

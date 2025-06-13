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
    public ResponseEntity<GeoLabelListDto> getGeoLabelsByCriteria(GeoLabelCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }

    @Override
    public ResponseEntity<Integer> createGeoLabel(UpdateGeoLabelDto updateGeoLabelDto) {
        return ResponseEntity.ok(service.createGeoLabel(updateGeoLabelDto));
    }

    @Override
    public ResponseEntity<Void> updateGeoLabel(Integer geoLabelId, UpdateGeoLabelDto updateGeoLabelDto) {
        service.updateLabel(geoLabelId, updateGeoLabelDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteGeoLabel(Integer geoLabelId) {
        service.delete(geoLabelId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GeoLabelListDto> getGeoLabelsForActivity(Integer activityId, GeoLabelCriteriaDto criteria) {
        if (criteria == null) {
            criteria = new GeoLabelCriteriaDto();
        }
        criteria.setActivityId(activityId);
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }
}

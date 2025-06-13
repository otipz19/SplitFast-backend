package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateGeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;


@Component
public class GeoLabelMerger implements IMerger<GeoLabelEntity, UpdateGeoLabelDto, UpdateGeoLabelDto> {

    @Override
    public void mergeForCreate(GeoLabelEntity entity, UpdateGeoLabelDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(GeoLabelEntity entity, UpdateGeoLabelDto view) {
        merge(entity, view);
    }

    private void merge(GeoLabelEntity entity, UpdateGeoLabelDto view) {
        entity.setName(view.getName());
        entity.setDescription(view.getDescription());
        entity.setGeolocation(view.getGeolocation());
    }
}

package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateActivityDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.GeoLabelRepository;

@Component
@RequiredArgsConstructor
public class ActivityMerger implements IMerger<ActivityEntity, UpdateActivityDto, UpdateActivityDto> {

    private final GeoLabelRepository geoLabelRepository;

    @Override
    public void mergeForCreate(ActivityEntity entity, UpdateActivityDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(ActivityEntity entity, UpdateActivityDto view) {
        merge(entity, view);
    }

    private void merge(ActivityEntity entity, UpdateActivityDto view) {
        entity.setName(view.getName());
        entity.setDescription(view.getDescription());
        if (view.getGeoLabelId() != null) {
            GeoLabelEntity geoLabel = geoLabelRepository.findById(view.getGeoLabelId())
                    .orElseThrow(() -> new ValidationException("error.activity.geo-label.not-exists"));
            entity.setGeoLabel(geoLabel);
        }
        else
            entity.setGeoLabel(null);
        entity.setGeoLabelId(view.getGeoLabelId());
    }
}

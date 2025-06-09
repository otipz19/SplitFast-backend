package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateActivityDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;

@Component
public class ActivityMerger implements IMerger<ActivityEntity, UpdateActivityDto, UpdateActivityDto> {

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
    }
}

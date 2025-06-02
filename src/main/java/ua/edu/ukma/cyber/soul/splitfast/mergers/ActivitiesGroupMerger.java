package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateActivitiesGroupDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;

@Component
public class ActivitiesGroupMerger implements IMerger<ActivitiesGroupEntity, UpdateActivitiesGroupDto> {

    @Override
    public void mergeForCreate(ActivitiesGroupEntity entity, UpdateActivitiesGroupDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(ActivitiesGroupEntity entity, UpdateActivitiesGroupDto view) {
        merge(entity, view);
    }

    private void merge(ActivitiesGroupEntity entity, UpdateActivitiesGroupDto view) {
        entity.setName(view.getName());
        entity.setDescription(view.getDescription());
    }
}

package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateUserDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

@Component
public class UserMerger implements IMerger<UserEntity, UpdateUserDto> {

    @Override
    public void mergeForCreate(UserEntity entity, UpdateUserDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(UserEntity entity, UpdateUserDto view) {
        merge(entity, view);
    }

    private void merge(UserEntity entity, UpdateUserDto view) {
        entity.setName(view.getName());
        entity.setEmail(view.getEmail());
        entity.setPhone(view.getPhone());
    }
}

package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateUserDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;

@Component
@RequiredArgsConstructor
public class UserMerger implements IMerger<UserEntity, CreateUserDto, UpdateUserDto> {

    private final PasswordEncoder passwordEncoder;
    private final EnumsMapper enumsMapper;

    @Override
    public void mergeForCreate(UserEntity entity, CreateUserDto view) {
        entity.setRole(enumsMapper.map(view.getRole()));
        entity.setUsername(view.getUsername());
        entity.setPasswordHash(passwordEncoder.encode(view.getPassword()));
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

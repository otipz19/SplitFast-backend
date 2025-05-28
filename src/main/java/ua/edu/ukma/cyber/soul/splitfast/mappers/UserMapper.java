package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends IMapper<UserEntity, UserDto> {
}

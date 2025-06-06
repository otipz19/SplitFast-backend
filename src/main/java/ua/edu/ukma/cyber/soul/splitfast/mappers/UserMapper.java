package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends IResponseMapper<UserEntity, UserDto>, IListResponseMapper<UserEntity, UserListDto>,
        IShortResponseMapper<UserEntity, ShortUserDto> {

    @Mapping(target = "role", constant = "USER")
    CreateUserDto toCreateUserDto(RegisterUserDto registerUserDto);
}

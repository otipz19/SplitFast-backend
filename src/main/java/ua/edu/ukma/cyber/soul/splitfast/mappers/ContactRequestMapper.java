package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ContactRequestMapper extends IResponseMapper<ContactRequestEntity, ContactRequestDto>,
        IListResponseMapper<ContactRequestEntity, ContactRequestListDto> {

    @Mapping(target = "fromUser", source = "usersAssociation.fromUser")
    @Mapping(target = "toUser", source = "usersAssociation.toUser")
    ContactRequestDto toResponse(ContactRequestEntity entity);

}

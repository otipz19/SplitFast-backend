package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;

@Mapper(config = MapperConfiguration.class)
public interface ContactRequestMapper extends IMapper<ContactRequestEntity, ContactRequestDto> {
}

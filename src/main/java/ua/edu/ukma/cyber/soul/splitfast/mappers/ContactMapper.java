package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;

@Mapper(config = MapperConfiguration.class)
public interface ContactMapper extends IMapper<ContactEntity, ContactDto> {

    @Mappings({
            @Mapping(target = "firstUserId", source = "id.firstUserId"),
            @Mapping(target = "secondUserId", source = "id.secondUserId"),
            @Mapping(source = "firstHistoryDebt", target = "firstHistoryDebt"),
            @Mapping(source = "firstCurrentDebt", target = "firstCurrentDebt"),
            @Mapping(source = "secondHistoryDebt", target = "secondHistoryDebt"),
            @Mapping(source = "secondCurrentDebt", target = "secondCurrentDebt")
    })
    @Override
    ContactDto toResponse(ContactEntity entity);
}

package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserRoleDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        implementationPackage = "<PACKAGE_NAME>.generated"
)
public interface EnumsMapper {

    UserRole map(UserRoleDto userRoleDto);

    UserRoleDto map(UserRole userRoleDto);
}

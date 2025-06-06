package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberTypeDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserRoleDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        implementationPackage = "<PACKAGE_NAME>.generated"
)
public interface EnumsMapper {

    UserRole map(UserRoleDto userRoleDto);

    UserRoleDto map(UserRole userRole);

    String toString(UserRole userRole);

    @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = MappingConstants.ANY_REMAINING)
    UserRole fromString(String userRole);

    ExpenseMemberType map(ExpenseMemberTypeDto memberTypeDto);

    ExpenseMemberTypeDto map(ExpenseMemberType memberType);
}

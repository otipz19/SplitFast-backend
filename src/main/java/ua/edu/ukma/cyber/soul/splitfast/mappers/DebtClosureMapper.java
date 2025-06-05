package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosure;

@Mapper(config = MapperConfiguration.class)
public abstract class DebtClosureMapper {

    @Mapping(target = "executorUser", source = "association.firstUser")
    @Mapping(target = "debtorUser", source = "association.secondUser")
    public abstract DebtClosureDto toResponse(DebtClosure entity);
}
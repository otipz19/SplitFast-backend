package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface DebtClosureMapper extends IResponseMapper<DebtClosureEntity, DebtClosureDto>, IListResponseMapper<DebtClosureEntity, DebtClosureListDto> {

    @Mapping(target = "fromUser", source = "usersAssociation.fromUser")
    @Mapping(target = "toUser", source = "usersAssociation.toUser")
    DebtClosureDto toResponse(DebtClosureEntity entity);
}
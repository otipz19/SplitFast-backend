package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface DebtRepaymentRequestMapper extends IResponseMapper<DebtRepaymentRequestEntity, DebtRepaymentRequestDto>,
        IListResponseMapper<DebtRepaymentRequestEntity, DebtRepaymentRequestListDto> {

    @Mapping(target = "fromUser", source = "usersAssociation.fromUser")
    @Mapping(target = "toUser", source = "usersAssociation.toUser")
    DebtRepaymentRequestDto toResponse(DebtRepaymentRequestEntity entity);
}
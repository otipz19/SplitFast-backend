package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest;

import java.util.List;

@Mapper(config = MapperConfiguration.class, uses = {UserMapper.class})
public abstract class DebtRepaymentRequestMapper {

    @Mapping(target = "fromUser", source = "association.firstUser")
    @Mapping(target = "toUser", source = "association.secondUser")
    public abstract DebtRepaymentRequestDto toResponse(DebtRepaymentRequest entity);

    public DebtRepaymentRequestListDto toListDto(long total, List<DebtRepaymentRequest> items) {
        DebtRepaymentRequestListDto listDto = new DebtRepaymentRequestListDto();
        listDto.setTotal((int) total);
        listDto.setItems(items.stream().map(this::toResponse).toList());
        return listDto;
    }
}
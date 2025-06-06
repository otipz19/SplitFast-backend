package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ExpenseMemberMapper extends IResponseMapper<ExpenseMemberEntity, ExpenseMemberDto>,
        IListResponseMapper<ExpenseMemberEntity, ExpenseMemberListDto> {
}

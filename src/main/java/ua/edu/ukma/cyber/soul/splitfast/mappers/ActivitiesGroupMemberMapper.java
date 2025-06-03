package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ActivitiesGroupMemberMapper extends IResponseMapper<ActivitiesGroupMemberEntity, ActivitiesGroupMemberDto>,
        IListResponseMapper<ActivitiesGroupMemberEntity, ActivitiesGroupMemberListDto> {

    @Mapping(target = "isOwner", source = "owner")
    ActivitiesGroupMemberDto toResponse(ActivitiesGroupMemberEntity entity);
}

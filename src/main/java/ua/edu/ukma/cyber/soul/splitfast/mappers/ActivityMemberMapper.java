package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivityMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ActivityMemberMapper extends IResponseMapper<ActivityMemberEntity, ActivityMemberDto>,
        IListResponseMapper<ActivityMemberEntity, ActivityMemberListDto> {

    @Mapping(target = "isOwner", source = "owner")
    ActivityMemberDto toResponse(ActivityMemberEntity entity);
}

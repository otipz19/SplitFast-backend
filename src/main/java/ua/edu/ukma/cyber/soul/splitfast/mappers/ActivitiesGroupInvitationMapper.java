package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupInvitationListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;

@Mapper(config = MapperConfiguration.class, uses = {UserMapper.class, ActivitiesGroupMapper.class})
public interface ActivitiesGroupInvitationMapper extends IResponseMapper<ActivitiesGroupInvitationEntity, ActivitiesGroupInvitationDto>,
        IListResponseMapper<ActivitiesGroupInvitationEntity, ActivitiesGroupInvitationListDto> {

    @Mapping(target = "fromUser", source = "usersAssociation.fromUser")
    @Mapping(target = "toUser", source = "usersAssociation.toUser")
    ActivitiesGroupInvitationDto toResponse(ActivitiesGroupInvitationEntity entity);

}

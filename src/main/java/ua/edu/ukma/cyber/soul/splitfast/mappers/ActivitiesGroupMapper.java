package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ActivitiesGroupListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ShortActivitiesGroupDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;

@Mapper(config = MapperConfiguration.class)
public interface ActivitiesGroupMapper extends IResponseMapper<ActivitiesGroupEntity, ActivitiesGroupDto>, IListResponseMapper<ActivitiesGroupEntity, ActivitiesGroupListDto>,
        IShortResponseMapper<ActivitiesGroupEntity, ShortActivitiesGroupDto> {
}

package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;

@Mapper(config = MapperConfiguration.class)
public interface ActivityMapper extends IResponseMapper<ActivityEntity, ActivityDto>, IListResponseMapper<ActivityEntity, ActivityListDto>,
        IShortResponseMapper<ActivityEntity, ShortActivityDto> {
}

package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelListDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;

@Mapper(config = MapperConfiguration.class)
public interface GeoLabelMapper extends IListResponseMapper<GeoLabelEntity, GeoLabelListDto>{


    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "activityId", source = "activity.id")
    GeoLabelDto toResponse(GeoLabelEntity entity);

}

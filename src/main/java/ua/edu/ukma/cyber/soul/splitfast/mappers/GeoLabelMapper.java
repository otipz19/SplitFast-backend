package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.GeoLabelListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ShortGeoLabelDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface GeoLabelMapper extends IResponseMapper<GeoLabelEntity, GeoLabelDto>, IShortResponseMapper<GeoLabelEntity, ShortGeoLabelDto>,
        IListResponseMapper<GeoLabelEntity, GeoLabelListDto>{

}

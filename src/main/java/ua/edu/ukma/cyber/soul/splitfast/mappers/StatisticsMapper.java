package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;

import java.util.List;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface StatisticsMapper extends IResponseMapper<AggregatedDebt, DebtDistributionDto> {

    List<DebtDistributionDto> toResponses(List<? extends AggregatedDebt> entities);
}

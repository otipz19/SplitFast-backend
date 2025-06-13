package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Mapper;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.HonoraryUsersDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;

import java.math.BigDecimal;
import java.util.List;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface StatisticsMapper {

    List<DebtDistributionDto> toDebtDistribution(List<? extends AggregatedDebt> entities);

    HonoraryUsersDto toHonoraryUsers(UserEntity topBorrower, BigDecimal topBorrowedAmount, UserEntity topLender, BigDecimal topLentAmount);
}

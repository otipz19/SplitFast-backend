package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.HonoraryUsersDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserBalanceDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface StatisticsMapper {

    List<DebtDistributionDto> toDebtDistribution(List<? extends AggregatedDebt> entities);

    HonoraryUsersDto toHonoraryUsers(UserEntity topBorrower, BigDecimal topBorrowedAmount, UserEntity topLender, BigDecimal topLentAmount);

    @Mapping(target = "borrowed", expression = "java(balanceMaps.borrowings().getOrDefault(user.getId(), BigDecimal.ZERO))")
    @Mapping(target = "lent", expression = "java(balanceMaps.lendings().getOrDefault(user.getId(), BigDecimal.ZERO))")
    UserBalanceDto toUserBalance(UserEntity user, @Context BalanceMaps balanceMaps);

    List<UserBalanceDto> toUsersBalances(List<UserEntity> users, @Context BalanceMaps balanceMaps);

    record BalanceMaps(Map<Integer, BigDecimal> borrowings, Map<Integer, BigDecimal> lendings) {}
}

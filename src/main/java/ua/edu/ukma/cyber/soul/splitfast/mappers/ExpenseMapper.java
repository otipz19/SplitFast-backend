package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ShortExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ExpenseMapper {

    @Mapping(target = "cost", expression = "java(costMap.getOrDefault(entity.getId(), BigDecimal.ZERO))")
    ExpenseDto toResponse(ExpenseEntity entity, @Context Map<Integer, BigDecimal> costMap);

    @Mapping(target = "cost", expression = "java(costMap.getOrDefault(entity.getId(), BigDecimal.ZERO))")
    ShortExpenseDto toShortResponse(ExpenseEntity entity, @Context Map<Integer, BigDecimal> costMap);

    ExpenseListDto toListResponse(long total, List<ExpenseEntity> items, @Context Map<Integer, BigDecimal> costMap);

}

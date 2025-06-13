package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseAggregatedDebtEntity;

import java.util.List;

public interface ExpenseAggregatedDebtRepository extends IRepository<ExpenseAggregatedDebtEntity, Integer> {

    List<ExpenseAggregatedDebtEntity> findByExpenseId(int expenseId);

    @EntityGraph(attributePaths = {"firstUser", "secondUser"})
    List<ExpenseAggregatedDebtEntity> findFullByExpenseId(int expenseId);
}

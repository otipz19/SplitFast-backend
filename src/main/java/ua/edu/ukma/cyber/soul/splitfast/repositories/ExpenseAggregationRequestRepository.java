package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseAggregationRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;

import java.util.List;

public interface ExpenseAggregationRequestRepository extends IRepository<ExpenseAggregationRequestEntity, Integer> {

    @Query(value = """
        SELECT *
        FROM expense_aggregation_requests
        WHERE expense_id = :expenseId
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    ExpenseAggregationRequestEntity findForUpdateByExpenseId(@Param("expenseId") int expenseId);

    @Query(value = """
        SELECT e.*
        FROM expense_aggregation_requests r
            JOIN expenses e ON r.expense_id = e.id
        FOR UPDATE OF r SKIP LOCKED
    """, nativeQuery = true)
    List<ExpenseEntity> findUnprocessedExpenses();

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM ExpenseAggregationRequestEntity r
            WHERE r.expenseId IN (
                SELECT e.id
                FROM ExpenseEntity e
                WHERE e.activityId = :activityId
            )
        )
    """)
    boolean existsByActivityId(@Param("activityId") int activityId);
}

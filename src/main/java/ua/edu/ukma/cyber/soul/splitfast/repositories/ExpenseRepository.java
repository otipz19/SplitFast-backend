package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends IRepository<ExpenseEntity, Integer> {

    boolean existsByActivity(ActivityEntity activity);

    @Query("""
        SELECT m.expenseId AS id, SUM(m.share) AS cost
        FROM ExpenseMemberEntity m
        WHERE m.expenseId IN :ids AND m.type = :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType).SHAREHOLDER}
        GROUP BY m.expenseId
    """)
    List<ExpenseCost> calculateExpensesCosts(@Param("ids") List<Integer> ids);

    interface ExpenseCost {
        Integer getId();
        BigDecimal getCost();
    }

    @Query("""
        SELECT COALESCE (
            (
                SELECT SUM(m.share)
                FROM ExpenseMemberEntity m
                WHERE m.expenseId IN :ids AND m.type = :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType).SHAREHOLDER}
            ), 0
        )
    """)
    BigDecimal calculateExpensesTotalCost(@Param("ids") List<Integer> ids);

    @Query("""
        SELECT e.id
        FROM ExpenseEntity e
        WHERE e.activityId = :activityId AND e.timeFinished IS NOT NULL
    """)
    List<Integer> findFinishedExpenseIdsByActivityId(@Param("activityId") int activityId);

    @Query("""
        SELECT e.id
        FROM ExpenseEntity e
        WHERE e.activityId IN (
            SELECT a.id
            FROM ActivityEntity a
            WHERE a.activitiesGroupId = :activitiesGroupId AND a.timeFinished IS NOT NULL
        )
    """)
    List<Integer> findFinishedExpenseIdsByActivitiesGroupId(@Param("activitiesGroupId") int activitiesGroupId);

    @Query("""
        SELECT
            SUM (
                CASE m.type
                     WHEN :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType).SHAREHOLDER} THEN m.share
                     ELSE 0
                END
            ) AS shareholdersCost,
            SUM (
                CASE m.type
                     WHEN :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType).BENEFICIARY} THEN m.share
                     ELSE 0
                END
            ) AS beneficiariesCost
        FROM ExpenseMemberEntity m
        WHERE m.expenseId = :id
    """)
    ExpenseState calculateExpenseState(@Param("id") int id);

    interface ExpenseState {
        BigDecimal getShareholdersCost();
        BigDecimal getBeneficiariesCost();
    }
}

package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType;

import java.util.List;
import java.util.Optional;

public interface ExpenseMemberRepository extends IRepository<ExpenseMemberEntity, Integer> {

    Optional<ExpenseMemberEntity> findByUserIdAndExpenseIdAndType(int userId, int expenseId, ExpenseMemberType type);

    void deleteAllByExpenseId(int expenseId);

    List<ExpenseMemberEntity> findAllByExpense(ExpenseEntity expense);
}

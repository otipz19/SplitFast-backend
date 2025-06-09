package ua.edu.ukma.cyber.soul.splitfast.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
@RequiredArgsConstructor
public class ExpenseUtils {

    private final ExpenseMemberRepository expenseMemberRepository;
    private final SecurityUtils securityUtils;

    public boolean isCurrentUserOwnerOf(ExpenseEntity expense) {
        return isUserOwnerOf(securityUtils.getCurrentUser(), expense);
    }

    public boolean isUserOwnerOf(UserEntity user, ExpenseEntity expense) {
        return user.getId() == expense.getOwnerId();
    }

    public boolean isUserMemberOf(UserEntity user, ExpenseEntity expense, ExpenseMemberType type) {
        return isUserMemberOf(user.getId(), expense.getId(), type);
    }

    public boolean isUserMemberOf(int userId, int expenseId, ExpenseMemberType type) {
        return expenseMemberRepository.findByUserIdAndExpenseIdAndType(userId, expenseId, type).isPresent();
    }


    public boolean isFinished(ExpenseEntity expense) {
        return expense.getTimeFinished() != null;
    }
}

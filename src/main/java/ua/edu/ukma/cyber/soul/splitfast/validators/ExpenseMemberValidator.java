package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ExpenseUtils;

@Component
public class ExpenseMemberValidator extends BaseValidator<ExpenseMemberEntity> {

    private final ActivityUtils activityUtils;
    private final ExpenseUtils expenseUtils;

    public ExpenseMemberValidator(Validator validator, SecurityUtils securityUtils, ActivityUtils activityUtils, ExpenseUtils expenseUtils) {
        super(validator, securityUtils);
        this.activityUtils = activityUtils;
        this.expenseUtils = expenseUtils;
    }

    @Override
    public void validForCreate(ExpenseMemberEntity entity) {
        validatePermissions(entity);
        validateData(entity);
        if (!activityUtils.isUserMemberOf(entity.getUser(), entity.getExpense().getActivityId()))
            throw new ValidationException("error.expense-member.user.not-in-activity");
        if (expenseUtils.isUserMemberOf(entity.getUser(), entity.getExpense(), entity.getType()))
            throw new ValidationException("error.expense-member.duplicate");
        requireNotFinishedExpense(entity);
    }

    @Override
    public void validForUpdate(ExpenseMemberEntity entity) {
        validatePermissions(entity);
        validateData(entity);
        requireNotFinishedExpense(entity);
    }

    @Override
    public void validForDelete(ExpenseMemberEntity entity) {
        validatePermissions(entity);
        requireNotFinishedExpense(entity);
    }

    private void validatePermissions(ExpenseMemberEntity entity) {
        if (securityUtils.hasRole(UserRole.SUPER_ADMIN, UserRole.ADMIN))
            return;
        if (entity.getUserId() == securityUtils.getCurrentUserId())
            return;
        if (expenseUtils.isCurrentUserOwnerOf(entity.getExpense()))
            return;
        throw new ForbiddenException();
    }

    private void requireNotFinishedExpense(ExpenseMemberEntity entity) {
        if (expenseUtils.isFinished(entity.getExpense()))
            throw new ValidationException("error.expense-member.expense.finished");
    }
}

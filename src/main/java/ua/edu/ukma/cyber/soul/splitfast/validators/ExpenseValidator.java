package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ActivityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.ExpenseUtils;

import java.util.List;
import java.util.Objects;

@Component
public class ExpenseValidator extends BaseValidator<ExpenseEntity> {

    private final ActivityUtils activityUtils;
    private final ExpenseUtils expenseUtils;
    private final ExpenseRepository expenseRepository;

    public ExpenseValidator(Validator validator, SecurityUtils securityUtils, ActivityUtils activityUtils, ExpenseUtils expenseUtils, ExpenseRepository expenseRepository) {
        super(validator, securityUtils);
        this.activityUtils = activityUtils;
        this.expenseUtils = expenseUtils;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void validForView(List<ExpenseEntity> entities) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        entities.stream()
                .map(ExpenseEntity::getActivityId)
                .distinct()
                .filter(activityId -> !activityUtils.isCurrentUserMemberOf(activityId))
                .findAny()
                .ifPresent(i -> { throw new ForbiddenException(); });
    }

    @Override
    public void validForView(ExpenseEntity entity) {
        if (securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN))
            return;
        if (!activityUtils.isCurrentUserMemberOf(entity.getActivityId()))
            throw new ForbiddenException();
    }

    @Override
    public void validForCreate(ExpenseEntity entity) {
        if (entity.getOwner().getRole() != UserRole.USER)
            throw new ValidationException("error.expense.member.admin");
        if (!activityUtils.isCurrentUserMemberOf(entity.getActivity()))
            throw new ForbiddenException();
        if (activityUtils.isFinished(entity.getActivity()))
            throw new ValidationException("error.activity.finished");
        validateData(entity);
    }

    @Override
    public void validForUpdate(ExpenseEntity entity) {
        requireAdminOrOwner(entity);
        validateData(entity);
        requireNotFinished(entity);
    }

    @Override
    public void validForDelete(ExpenseEntity entity) {
        requireAdminOrOwner(entity);
        requireNotFinished(entity);
    }

    public void validForFinish(ExpenseEntity entity) {
        requireAdminOrOwner(entity);
        requireNotFinished(entity);
        ExpenseRepository.ExpenseState state = expenseRepository.calculateExpenseState(entity.getId());
        if (!Objects.equals(state.getShareholdersCost(), state.getBeneficiariesCost()))
            throw new ValidationException("error.expense.costs.not-equal");
    }

    private void requireAdminOrOwner(ExpenseEntity entity) {
        if (!securityUtils.hasRole(UserRole.ADMIN, UserRole.SUPER_ADMIN) && !expenseUtils.isCurrentUserOwnerOf(entity))
            throw new ForbiddenException();
    }

    private void requireNotFinished(ExpenseEntity entity) {
        if (expenseUtils.isFinished(entity))
            throw new ValidationException("error.expense.finished");
    }
}

package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class DebtRepaymentRequestValidator extends BaseValidator<DebtRepaymentRequest> {

    public DebtRepaymentRequestValidator(Validator validator, SecurityUtils securityUtils) {
        super(validator, securityUtils);
    }

    @Override
    public void validForView(DebtRepaymentRequest entity) {
        int currentUserId = securityUtils.getCurrentUser().getId();
        if (entity.getAssociation().getFirstUser().getId().equals(currentUserId) || entity.getAssociation().getSecondUser().getId().equals(currentUserId)) {
            return;
        }
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }

    public void validateDebtRepaymentRequestAmount(BigDecimal requestAmount, BigDecimal totalAvailableForRequest) {
        if (requestAmount.compareTo(totalAvailableForRequest) > 0) {
            throw new ValidationException("error.debt-request-amount-gretear-available");
        }
    }

    public void validate(DebtRepaymentRequest entity) {
        validateData(entity);
        if (Objects.equals(entity.getAssociation().getFirstUser().getId(), entity.getAssociation().getSecondUser().getId())) {
            throw new ValidationException("error.debt-request.self-payment");
        }
    }
}
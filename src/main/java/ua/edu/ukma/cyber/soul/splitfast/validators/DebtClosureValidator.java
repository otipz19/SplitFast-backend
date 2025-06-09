package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.DebtRepaymentRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactService;

import java.math.BigDecimal;

@Component
public class DebtClosureValidator extends BaseValidator<DebtClosureEntity> {

    private final ContactService contactService;
    private final DebtRepaymentRequestRepository debtRepaymentRequestRepository;

    public DebtClosureValidator(Validator validator, SecurityUtils securityUtils, ContactService contactService, DebtRepaymentRequestRepository debtRepaymentRequestRepository) {
        super(validator, securityUtils);
        this.contactService = contactService;
        this.debtRepaymentRequestRepository = debtRepaymentRequestRepository;
    }

    @Override
    public void validForView(DebtClosureEntity entity) {
        int currentUserId = securityUtils.getCurrentUserId();
        if (entity.getUsersAssociation().getFromUserId() == currentUserId || entity.getUsersAssociation().getToUserId() == currentUserId)
            return;
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }

    @Override
    public void validForCreate(DebtClosureEntity entity) {
        validateData(entity);
        if (debtRepaymentRequestRepository.existsPendingRequestByUsersAssociation(entity.getUsersAssociation()))
            throw new ValidationException("error.debt-closure.exists-pending-debt-repayment-request");
        BigDecimal currentDebt = contactService.getEffectiveDebt(entity.getUsersAssociation());
        if (entity.getAmount().compareTo(currentDebt) > 0)
            throw new ValidationException("error.debt-closure.amount.too-high");
    }
}
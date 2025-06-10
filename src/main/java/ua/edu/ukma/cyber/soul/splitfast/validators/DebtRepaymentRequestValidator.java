package ua.edu.ukma.cyber.soul.splitfast.validators;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.DebtRepaymentRequestStatus;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.DebtRepaymentRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.services.ContactService;

import java.math.BigDecimal;

@Component
public class DebtRepaymentRequestValidator extends BaseValidator<DebtRepaymentRequestEntity> {

    private final ContactService contactService;
    private final DebtRepaymentRequestRepository repository;

    public DebtRepaymentRequestValidator(Validator validator, SecurityUtils securityUtils, ContactService contactService, DebtRepaymentRequestRepository repository) {
        super(validator, securityUtils);
        this.contactService = contactService;
        this.repository = repository;
    }

    @Override
    public void validForView(DebtRepaymentRequestEntity entity) {
        validateViewPermissions(entity.getUsersAssociation());
    }

    @Override
    public void validForCreate(DebtRepaymentRequestEntity entity) {
        validateData(entity);
        BigDecimal currentDebt = contactService.getEffectiveDebt(entity.getUsersAssociation());
        BigDecimal pendingAmount = repository.sumPendingRequestsAmount(entity.getUsersAssociation());
        BigDecimal available = currentDebt.subtract(pendingAmount);
        if (entity.getAmount().compareTo(available) > 0)
            throw new ValidationException("error.debt-repayment-request.amount.too-high");
    }

    @Override
    public void validForUpdate(DebtRepaymentRequestEntity entity) {
        if (entity.getUsersAssociation().getToUserId() != securityUtils.getCurrentUserId())
            throw new ForbiddenException();
        if (entity.getStatus() != DebtRepaymentRequestStatus.PENDING)
            throw new ValidationException("error.debt-repayment-request.status.not-pending");
    }

    public void validForViewPendingRequestsAmount(TwoUsersDirectedAssociation association) {
        validateViewPermissions(association);
    }

    private void validateViewPermissions(TwoUsersDirectedAssociation association) {
        int currentUserId = securityUtils.getCurrentUserId();
        if (association.getFromUserId() == currentUserId || association.getToUserId() == currentUserId)
            return;
        securityUtils.requireRole(UserRole.SUPER_ADMIN, UserRole.ADMIN);
    }
}
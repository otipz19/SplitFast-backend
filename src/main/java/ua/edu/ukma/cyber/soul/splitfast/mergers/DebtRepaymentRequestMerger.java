package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DebtRepaymentRequestMerger implements IMerger<DebtRepaymentRequestEntity, CreateDebtRepaymentRequestDto, Void> {

    private final UserRepository userRepository;

    @Override
    public void mergeForCreate(DebtRepaymentRequestEntity entity, CreateDebtRepaymentRequestDto view) {
        UserEntity toUser = userRepository.findById(view.getToUserId())
                .orElseThrow(() -> new ValidationException("error.debt-repayment-request.to-user.not-exists"));
        TwoUsersDirectedAssociation association = new TwoUsersDirectedAssociation();
        association.setToUser(toUser);
        association.setToUserId(toUser.getId());
        entity.setUsersAssociation(association);

        entity.setAmount(view.getAmount());
    }

    @Override
    public void mergeForUpdate(DebtRepaymentRequestEntity entity, Void view) {
        throw new UnsupportedOperationException();
    }
}

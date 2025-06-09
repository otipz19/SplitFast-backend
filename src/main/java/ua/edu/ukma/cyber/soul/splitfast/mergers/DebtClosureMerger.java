package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosureEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DebtClosureMerger implements IMerger<DebtClosureEntity, CreateDebtClosureDto, Void> {

    private final UserRepository userRepository;

    @Override
    public void mergeForCreate(DebtClosureEntity entity, CreateDebtClosureDto view) {
        UserEntity fromUser = userRepository.findById(view.getFromUserId())
                .orElseThrow(() -> new ValidationException("error.debt-closure.from-user.not-exists"));
        TwoUsersDirectedAssociation association = new TwoUsersDirectedAssociation();
        association.setFromUser(fromUser);
        association.setFromUserId(fromUser.getId());
        entity.setUsersAssociation(association);

        entity.setAmount(view.getAmount());
    }

    @Override
    public void mergeForUpdate(DebtClosureEntity entity, Void view) {
        throw new UnsupportedOperationException();
    }
}

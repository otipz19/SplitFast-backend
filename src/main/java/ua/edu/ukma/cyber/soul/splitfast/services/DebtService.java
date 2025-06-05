package ua.edu.ukma.cyber.soul.splitfast.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final ContactRepository contactRepository;

    public BigDecimal getDebtAmount(int fromUserId, int toUserId) {
        ContactEntity contact = getContact(fromUserId, toUserId);

        if (contact.getUsersAssociation().getFirstUserId() == fromUserId) {
            return contact.getFirstCurrentDebt();
        } else {
            return contact.getSecondCurrentDebt();
        }
    }

    @Transactional
    public void deductDebt(int fromUserId, int toUserId, BigDecimal amount) {
        ContactEntity contact = getContact(fromUserId, toUserId);

        if (contact.getUsersAssociation().getFirstUserId() == fromUserId) {
            contact.setFirstCurrentDebt(contact.getFirstCurrentDebt().subtract(amount));
        } else {
            contact.setSecondCurrentDebt(contact.getSecondCurrentDebt().subtract(amount));
        }

        contactRepository.save(contact);
    }


    @Transactional
    public void increaseHistoricalDebt(int fromUserId, int toUserId, BigDecimal amount) {
        ContactEntity contact = getContact(fromUserId, toUserId);

        if (contact.getUsersAssociation().getFirstUserId() == fromUserId) {
            contact.setFirstHistoricalDebt(contact.getFirstHistoricalDebt().add(amount));
        } else {
            contact.setSecondHistoricalDebt(contact.getSecondHistoricalDebt().add(amount));
        }

        contactRepository.save(contact);
    }
    @Transactional
    public void transferAllCurrentToHistoricalDebt(int userId1, int userId2) {
        ContactEntity contact = getContact(userId1, userId2);

        if (contact.getUsersAssociation().getFirstUserId() == userId1) {
            contact.setFirstHistoricalDebt(contact.getFirstHistoricalDebt().add(contact.getFirstCurrentDebt()));
            contact.setFirstCurrentDebt(BigDecimal.ZERO);

            contact.setSecondHistoricalDebt(contact.getSecondHistoricalDebt().add(contact.getSecondCurrentDebt()));
            contact.setSecondCurrentDebt(BigDecimal.ZERO);
        } else {
            contact.setSecondHistoricalDebt(contact.getSecondHistoricalDebt().add(contact.getSecondCurrentDebt()));
            contact.setSecondCurrentDebt(BigDecimal.ZERO);

            contact.setFirstHistoricalDebt(contact.getFirstHistoricalDebt().add(contact.getFirstCurrentDebt()));
            contact.setFirstCurrentDebt(BigDecimal.ZERO);
        }

        contactRepository.save(contact);
    }

    private ContactEntity getContact(int userId1, int userId2) {
        return contactRepository.findByUsers(userId1, userId2)
                .orElseThrow(() -> new NotFoundException(ContactEntity.class, "users: " + userId1 + ", " + userId2));
    }
}

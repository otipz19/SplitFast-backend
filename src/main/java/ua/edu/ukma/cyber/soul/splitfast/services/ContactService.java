package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ContactCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseAggregatedDebtEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ContactMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ContactRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ContactValidator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository repository;
    private final CriteriaRepository criteriaRepository;
    private final ContactMapper mapper;
    private final ContactValidator validator;
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    public ContactListDto getListResponseByCriteria(ContactCriteriaDto criteriaDto) {
        ContactCriteria criteria = new ContactCriteria(criteriaDto);
        List<ContactEntity> entities = criteriaRepository.find(criteria);
        validator.validForView(entities);
        long total = criteriaRepository.count(criteria);
        return mapper.toListResponse(criteriaDto.getThisUserId(), total, entities);
    }

    @Transactional
    public void setIsMarked(int contactId, boolean isMarked) {
        ContactEntity contact = repository.findById(contactId)
                .orElseThrow(() -> new NotFoundException(ContactEntity.class, "id: " + contactId));
        int currentUserId = securityUtils.getCurrentUserId();
        if (contact.getUsersAssociation().getFirstUserId() == currentUserId)
            contact.setFirstIsMarked(isMarked);
        else if (contact.getUsersAssociation().getSecondUserId() == currentUserId)
            contact.setSecondIsMarked(isMarked);
        else
            throw new ForbiddenException();
        repository.save(contact);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public BigDecimal getEffectiveDebt(TwoUsersDirectedAssociation association) {
        ContactEntity contact = repository.findByTwoUsersDirectedAssociation(association).orElse(null);
        if (contact == null) return BigDecimal.ZERO;
        BigDecimal firstDebt = contact.getFirstCurrentDebt();
        BigDecimal secondDebt = contact.getSecondCurrentDebt();
        if (contact.getUsersAssociation().getFirstUserId() == association.getFromUserId())
            return firstDebt.subtract(secondDebt);
        else
            return secondDebt.subtract(firstDebt);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateContact(TwoUsersDirectedAssociation association, BigDecimal amount) {
        ContactEntity contact = repository.findByTwoUsersDirectedAssociation(association).orElseThrow(IllegalStateException::new);
        BigDecimal firstDebt = contact.getFirstCurrentDebt();
        BigDecimal secondDebt = contact.getSecondCurrentDebt();
        if (contact.getUsersAssociation().getFirstUserId() == association.getFromUserId()) {
            BigDecimal delta = calculateDelta(firstDebt, amount);
            BigDecimal overflow = calculateOverflow(firstDebt, amount);
            contact.setFirstCurrentDebt(firstDebt.subtract(delta));
            contact.setFirstHistoricalDebt(contact.getFirstHistoricalDebt().add(delta));
            contact.setSecondCurrentDebt(secondDebt.add(overflow));
        } else {
            BigDecimal delta = calculateDelta(secondDebt, amount);
            BigDecimal overflow = calculateOverflow(secondDebt, amount);
            contact.setSecondCurrentDebt(secondDebt.subtract(delta));
            contact.setSecondHistoricalDebt(contact.getSecondHistoricalDebt().add(delta));
            contact.setFirstCurrentDebt(firstDebt.add(overflow));
        }
        resetCurrentDebtIfEqual(contact);
        repository.save(contact);
    }

    private BigDecimal calculateDelta(BigDecimal current, BigDecimal requested) {
        if (current.compareTo(requested) >= 0)
            return requested;
        else
            return current;
    }

    private BigDecimal calculateOverflow(BigDecimal current, BigDecimal requested) {
        if (current.compareTo(requested) >= 0)
            return BigDecimal.ZERO;
        else
            return requested.subtract(current);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateContacts(Map<TwoUsersAssociation, ExpenseAggregatedDebtEntity> aggregatedDebts) {
        Map<TwoUsersAssociation, ContactEntity> contacts = findContacts(aggregatedDebts.keySet());
        for (Map.Entry<TwoUsersAssociation, ExpenseAggregatedDebtEntity> entry : aggregatedDebts.entrySet()) {
            AggregatedDebt aggregatedDebt = entry.getValue();
            ContactEntity contact = contacts.computeIfAbsent(entry.getKey(), ContactEntity::new);
            BigDecimal firstCurrentDebt = contact.getFirstCurrentDebt().add(aggregatedDebt.getFirstDebt());
            BigDecimal secondCurrentDebt = contact.getSecondCurrentDebt().add(aggregatedDebt.getSecondDebt());
            contact.setFirstCurrentDebt(firstCurrentDebt);
            contact.setSecondCurrentDebt(secondCurrentDebt);
            resetCurrentDebtIfEqual(contact);
        }
        repository.saveAll(contacts.values());
    }

    private Map<TwoUsersAssociation, ContactEntity> findContacts(Collection<TwoUsersAssociation> associations) {
        return repository.findAllByUsersAssociationIn(associations).stream()
                .collect(Collectors.toMap(ContactEntity::getUsersAssociation, Function.identity()));
    }

    private void resetCurrentDebtIfEqual(ContactEntity contact) {
        BigDecimal firstCurrentDebt = contact.getFirstCurrentDebt();
        BigDecimal secondCurrentDebt = contact.getSecondCurrentDebt();
        if (firstCurrentDebt.compareTo(secondCurrentDebt) == 0) {
            contact.setFirstHistoricalDebt(contact.getFirstHistoricalDebt().add(firstCurrentDebt));
            contact.setSecondHistoricalDebt(contact.getSecondHistoricalDebt().add(secondCurrentDebt));
            contact.setFirstCurrentDebt(BigDecimal.ZERO);
            contact.setSecondCurrentDebt(BigDecimal.ZERO);
        }
    }
}

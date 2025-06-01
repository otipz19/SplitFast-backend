package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.ContactId;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@Entity
@Table(name = "contacts")
@Data
public class ContactEntity implements IGettableById<ContactId> {
    @EmbeddedId
    private ContactId id;

    @NotNull(message = "error.contact.firstHistoryDebt.null")
    @Column(name = "first_history_debt", nullable = false)
    private Double firstHistoryDebt;

    @NotNull(message = "error.contact.firstCurrentDebt.null")
    @Column(name = "first_current_debt", nullable = false)
    private Double firstCurrentDebt;

    @NotNull(message = "error.contact.secondHistoryDebt.null")
    @Column(name = "second_history_debt", nullable = false)
    private Double secondHistoryDebt;

    @NotNull(message = "error.contact.secondCurrentDebt.null")
    @Column(name = "second_current_debt", nullable = false)
    private Double secondCurrentDebt;
}

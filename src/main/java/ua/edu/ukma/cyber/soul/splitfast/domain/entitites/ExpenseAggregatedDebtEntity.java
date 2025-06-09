package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.AggregatedDebt;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_aggregated_debts")
public class ExpenseAggregatedDebtEntity extends AggregatedDebt {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expense_id")
    private ExpenseEntity expense;

    @Column(name = "expense_id", nullable = false, insertable = false, updatable = false)
    private int expenseId;

    public ExpenseAggregatedDebtEntity(TwoUsersAssociation association, ExpenseEntity expense) {
        super(association);
        this.expense = expense;
    }
}

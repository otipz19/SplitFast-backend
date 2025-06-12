package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_aggregation_requests")
public class ExpenseAggregationRequestEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "expense_id")
    private int expenseId;

    @Override
    public Integer getId() {
        return expenseId;
    }
}

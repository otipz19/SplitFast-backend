package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.ExpenseMemberType;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_members")
public class ExpenseMemberEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expense_id")
    private ExpenseEntity expense;

    @Column(name = "expense_id", nullable = false, insertable = false, updatable = false)
    private int expenseId;

    @NotNull(message = "error.expense-member.type.null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExpenseMemberType type;

    @NotNull(message = "error.expense-member.share.null")
    @DecimalMin(value = "0.01", message = "error.expense-member.share.min")
    @DecimalMax(value = "99999999.99", message = "error.expense-member.share.max")
    @Column(name = "share", nullable = false)
    private BigDecimal share;
}

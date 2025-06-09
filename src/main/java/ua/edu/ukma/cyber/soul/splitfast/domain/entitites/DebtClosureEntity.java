package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debt_closures")
public class DebtClosureEntity implements IGettableById<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Valid
    @Embedded
    private TwoUsersDirectedAssociation usersAssociation;

    @NotNull(message = "error.debt-closure.amount.null")
    @DecimalMin(value = "0.01", message = "error.debt-closure.amount.min")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;
}

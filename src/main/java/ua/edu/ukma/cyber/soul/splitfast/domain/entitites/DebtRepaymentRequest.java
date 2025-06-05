package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debt_repayment_requests")
public class DebtRepaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Valid
    @Embedded
    private TwoUsersAssociation association;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sum", nullable = false)
    private BigDecimal amount;

    @Column(name = "is_submitted", nullable = false)
    private boolean submitted = false;

    @Column(name = "time_submitted")
    private LocalDateTime submittedAt;

}

package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class ContactEntity implements IGettableById<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Embedded
    private TwoUsersAssociation usersAssociation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_user_id", insertable = false, updatable = false)
    private UserEntity firstUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_user_id", insertable = false, updatable = false)
    private UserEntity secondUser;

    @Column(name = "first_historical_debt", nullable = false)
    private BigDecimal firstHistoricalDebt;

    @Column(name = "first_current_debt", nullable = false)
    private BigDecimal firstCurrentDebt;

    @Column(name = "first_is_marked", nullable = false)
    private boolean firstIsMarked;

    @Column(name = "second_historical_debt", nullable = false)
    private BigDecimal secondHistoricalDebt;

    @Column(name = "second_current_debt", nullable = false)
    private BigDecimal secondCurrentDebt;

    @Column(name = "second_is_marked", nullable = false)
    private boolean secondIsMarked;

    public ContactEntity(TwoUsersAssociation usersAssociation) {
        this.usersAssociation = usersAssociation;
        this.firstHistoricalDebt = BigDecimal.ZERO;
        this.firstCurrentDebt = BigDecimal.ZERO;
        this.secondHistoricalDebt = BigDecimal.ZERO;
        this.secondCurrentDebt = BigDecimal.ZERO;
    }
}

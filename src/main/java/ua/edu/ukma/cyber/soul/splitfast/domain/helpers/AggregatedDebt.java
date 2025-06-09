package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AggregatedDebt implements IGettableById<Integer> {

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

    @Column(name = "first_debt", nullable = false)
    private BigDecimal firstDebt;

    @Column(name = "second_debt", nullable = false)
    private BigDecimal secondDebt;

    public AggregatedDebt(TwoUsersAssociation usersAssociation) {
        this.usersAssociation = usersAssociation;
        this.firstDebt = BigDecimal.ZERO;
        this.secondDebt = BigDecimal.ZERO;
    }
}

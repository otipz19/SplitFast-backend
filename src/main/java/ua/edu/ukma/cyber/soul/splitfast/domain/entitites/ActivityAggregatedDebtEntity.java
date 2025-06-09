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
@Table(name = "activity_aggregated_debts")
public class ActivityAggregatedDebtEntity extends AggregatedDebt {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private ActivityEntity activity;

    @Column(name = "activity_id", nullable = false)
    private int activityId;

    public ActivityAggregatedDebtEntity(TwoUsersAssociation association, int activityId) {
        super(association);
        this.activityId = activityId;
    }
}

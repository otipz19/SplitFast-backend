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
@Table(name = "activities_group_aggregated_debts")
public class ActivitiesGroupAggregatedDebtEntity extends AggregatedDebt {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activities_group_id", insertable = false, updatable = false)
    private ActivitiesGroupEntity activitiesGroup;

    @Column(name = "activities_group_id", nullable = false)
    private int activitiesGroupId;

    public ActivitiesGroupAggregatedDebtEntity(TwoUsersAssociation association, int activitiesGroupId) {
        super(association);
        this.activitiesGroupId = activitiesGroupId;
    }
}

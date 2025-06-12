package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity_aggregation_requests")
public class ActivityAggregationRequestEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "activity_id")
    private int activityId;

    @Override
    public Integer getId() {
        return activityId;
    }
}

package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geo_labels")
public class GeoLabelEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activities_group_id")
    private ActivitiesGroupEntity activitiesGroup;

    @Column(name = "activities_group_id", nullable = false, insertable = false, updatable = false)
    private int activitiesGroupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(name = "owner_id", nullable = false, insertable = false, updatable = false)
    private int ownerId;

    @NotBlank(message = "error.geo-label.name.blank")
    @Size(max = 100, message = "error.geo-label.name.size")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "error.geo-label.description.size")
    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;
}

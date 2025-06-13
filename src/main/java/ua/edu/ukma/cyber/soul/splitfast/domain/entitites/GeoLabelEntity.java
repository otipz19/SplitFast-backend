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
@Table(name = "geo_label")
public class GeoLabelEntity implements IGettableById<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "error.label.name.blank")
    @Size(max = 100, message = "error.label.name.max")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 500, message = "error.label.description.max")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "error.label.geolocation.blank")
    @Size(max = 255, message = "error.label.geolocation.max")
    @Column(name = "geolocation")
    private String geolocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @Column(name = "creator_id", insertable = false, updatable = false)
    private int creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;

    @Column(name = "activity_id", insertable = false, updatable = false)
    private int activityId;

}

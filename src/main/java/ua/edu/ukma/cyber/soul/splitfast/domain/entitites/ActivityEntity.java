package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activities")
public class ActivityEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activities_group_id")
    private ActivitiesGroupEntity activitiesGroup;

    @Column(name = "activities_group_id", nullable = false, insertable = false, updatable = false)
    private int activitiesGroupId;

    @NotBlank(message = "error.activity.name.blank")
    @Size(max = 100, message = "error.activity.name.size")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "error.activity.description.size")
    @Column(name = "description")
    private String description;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @Column(name = "time_finished")
    private LocalDateTime timeFinished;
}

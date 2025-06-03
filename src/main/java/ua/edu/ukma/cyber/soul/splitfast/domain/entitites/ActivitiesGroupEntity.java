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
@Table(name = "activities_groups")
public class ActivitiesGroupEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "error.activities-group.name.blank")
    @Size(max = 100, message = "error.activities-group.name.size")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "error.activities-group.description.size")
    @Column(name = "description")
    private String description;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;
}

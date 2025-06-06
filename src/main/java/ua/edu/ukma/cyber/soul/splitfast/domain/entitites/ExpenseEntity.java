package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class ExpenseEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;

    @Column(name = "activity_id", nullable = false, insertable = false, updatable = false)
    private int activityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(name = "owner_id", nullable = false, insertable = false, updatable = false)
    private int ownerId;

    @NotBlank(message = "error.expense.name.blank")
    @Size(max = 100, message = "error.expense.name.size")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "error.expense.description.size")
    @Column(name = "description")
    private String description;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @Column(name = "time_finished")
    private LocalDateTime timeFinished;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "expense")
    private List<ExpenseMemberEntity> members;
}

package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activities_group_members")
public class ActivitiesGroupMemberEntity implements IGettableById<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activities_group_id")
    private ActivitiesGroupEntity activitiesGroup;

    @Column(name = "activities_group_id", nullable = false, insertable = false, updatable = false)
    private int activitiesGroupId;

    @Column(name = "is_owner", nullable = false)
    private boolean isOwner;
}

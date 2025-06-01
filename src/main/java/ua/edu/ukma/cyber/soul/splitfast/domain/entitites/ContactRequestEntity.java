package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_requests")
public class ContactRequestEntity implements IGettableById<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "error.contact.fromUserId.null")
    @Column(name = "from_user_id", nullable = false)
    private Integer fromUserId;

    @NotNull(message = "error.contact.toUserId.null")
    @Column(name = "to_user_id", nullable = false)
    private Integer toUserId;

    @NotNull(message = "error.contact.timeCreated.null")
    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;
}

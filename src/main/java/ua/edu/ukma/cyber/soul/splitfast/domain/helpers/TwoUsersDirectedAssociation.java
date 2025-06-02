package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.validators.constraints.DifferentUsers;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
@Access(AccessType.FIELD)
@DifferentUsers
public class TwoUsersDirectedAssociation implements Serializable, Comparable<TwoUsersDirectedAssociation> {

    @NotNull(message = "error.two-users-directed-association.from-user.null")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;

    @Column(name = "from_user_id", nullable = false, insertable = false, updatable = false)
    private int fromUserId;

    @NotNull(message = "error.two-users-directed-association.to-user.null")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "to_user_id")
    private UserEntity toUser;

    @Column(name = "to_user_id", nullable = false, insertable = false, updatable = false)
    private int toUserId;

    public TwoUsersDirectedAssociation(UserEntity from, UserEntity to) {
        this.fromUser = from;
        this.fromUserId = from.getId();
        this.toUser = to;
        this.toUserId = to.getId();
    }

    @Override
    public int compareTo(TwoUsersDirectedAssociation o) {
        int fromCompare = Integer.compare(this.fromUserId, o.fromUserId);
        if (fromCompare != 0) {
            return fromCompare;
        }
        return Integer.compare(this.toUserId, o.toUserId);
    }
}

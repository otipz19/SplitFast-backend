package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.validators.constraints.DifferentUsers;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@Access(AccessType.FIELD)
@DifferentUsers
public class TwoUsersAssociation implements Serializable, Comparable<TwoUsersAssociation> {

    @NotNull(message = "error.two-users-association.first-user.null")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "first_user_id")
    private UserEntity firstUser;

    @Column(name = "first_user_id", nullable = false, insertable = false, updatable = false)
    private int firstUserId;

    @NotNull(message = "error.two-users-association.second-user.null")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "second_user_id")
    private UserEntity secondUser;

    @Column(name = "second_user_id", nullable = false, insertable = false, updatable = false)
    private int secondUserId;

    public TwoUsersAssociation(UserEntity first, UserEntity second) {
        setUsers(first, second);
    }

    public TwoUsersAssociation(TwoUsersDirectedAssociation association) {
        setUsers(association.getFromUser(), association.getToUser());
    }

    public void setUsers(UserEntity first, UserEntity second) {
        if (first.getId() <= second.getId()) {
            this.firstUser = first;
            this.firstUserId = first.getId();
            this.secondUser = second;
            this.secondUserId = second.getId();
        } else {
            this.firstUser = second;
            this.firstUserId = second.getId();
            this.secondUser = first;
            this.secondUserId = first.getId();
        }
    }

    @Override
    public int compareTo(TwoUsersAssociation o) {
        int firstCompare = Integer.compare(this.firstUserId, o.firstUserId);
        if (firstCompare != 0) {
            return firstCompare;
        }
        return Integer.compare(this.secondUserId, o.secondUserId);
    }
}

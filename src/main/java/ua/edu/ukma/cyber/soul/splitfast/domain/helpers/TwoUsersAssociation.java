package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@Access(AccessType.FIELD)
public class TwoUsersAssociation implements Serializable, Comparable<TwoUsersAssociation> {

    @Column(name = "first_user_id", nullable = false)
    private int firstUserId;

    @Column(name = "second_user_id", nullable = false)
    private int secondUserId;

    public TwoUsersAssociation(int firstId, int secondId) {
        setUsers(firstId, secondId);
    }

    public void setUsers(int firstId, int secondId) {
        if (firstId <= secondId) {
            this.firstUserId = firstId;
            this.secondUserId = secondId;
        } else {
            this.firstUserId = secondId;
            this.secondUserId = firstId;
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

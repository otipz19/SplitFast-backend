package ua.edu.ukma.cyber.soul.splitfast.domain.helpers;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ContactId implements Serializable, Comparable<ContactId> {

    @Column(name = "first_user_id")
    private int firstUserId;

    @Column(name = "second_user_id")
    private int secondUserId;

    public ContactId(int userId1, int userId2) {
        if (userId1 <= userId2) {
            this.firstUserId = userId1;
            this.secondUserId = userId2;
        } else {
            this.firstUserId = userId2;
            this.secondUserId = userId1;
        }
    }
    @Override
    public int compareTo(ContactId o) {
        int firstCompare = Integer.compare(this.firstUserId, o.firstUserId);
        if (firstCompare != 0) {
            return firstCompare;
        }
        return Integer.compare(this.secondUserId, o.secondUserId);
    }
}

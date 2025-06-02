package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

public interface ContactRequestRepository extends IRepository<ContactRequestEntity, Integer> {

    boolean existsByUsersAssociation(TwoUsersDirectedAssociation association);

    @Modifying
    @Query("""
        DELETE FROM ContactRequestEntity
        WHERE (usersAssociation.fromUserId = :#{#association.fromUserId} AND usersAssociation.toUserId = :#{#association.toUserId}) OR
              (usersAssociation.fromUserId = :#{#association.toUserId} AND usersAssociation.toUserId = :#{#association.fromUserId})
    """)
    void deleteAllByUsersAssociation(TwoUsersDirectedAssociation association);

}


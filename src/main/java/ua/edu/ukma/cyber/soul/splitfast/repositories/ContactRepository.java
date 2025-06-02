package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

public interface ContactRepository extends IRepository<ContactEntity, Integer> {

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM ContactEntity
            WHERE (usersAssociation.firstUserId = :#{#association.fromUserId} AND usersAssociation.secondUserId = :#{#association.toUserId}) OR
                  (usersAssociation.firstUserId = :#{#association.toUserId} AND usersAssociation.secondUserId = :#{#association.fromUserId})
        )
    """)
    boolean existsByUsersAssociation(@Param("association") TwoUsersDirectedAssociation association);

}

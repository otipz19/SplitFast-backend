package ua.edu.ukma.cyber.soul.splitfast.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

import java.util.Collection;
import java.util.List;

import java.util.Optional;

public interface ContactRepository extends IRepository<ContactEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ContactEntity> findForUpdateByUsersAssociationIn(Collection<TwoUsersAssociation> associations);

    @Query("""
        SELECT c
        FROM ContactEntity c
        WHERE (c.usersAssociation.firstUserId = :#{#association.fromUserId} AND c.usersAssociation.secondUserId = :#{#association.toUserId}) OR
              (c.usersAssociation.firstUserId = :#{#association.toUserId} AND c.usersAssociation.secondUserId = :#{#association.fromUserId})
    """)
    Optional<ContactEntity> findByTwoUsersDirectedAssociation(@Param("association") TwoUsersDirectedAssociation association);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT c
        FROM ContactEntity c
        WHERE (c.usersAssociation.firstUserId = :#{#association.fromUserId} AND c.usersAssociation.secondUserId = :#{#association.toUserId}) OR
              (c.usersAssociation.firstUserId = :#{#association.toUserId} AND c.usersAssociation.secondUserId = :#{#association.fromUserId})
    """)
    Optional<ContactEntity> findForUpdateByTwoUsersDirectedAssociation(@Param("association") TwoUsersDirectedAssociation association);

    @Query(value = """
        SELECT pg_advisory_xact_lock(-1)
    """, nativeQuery = true)
    void lockInserts();

}

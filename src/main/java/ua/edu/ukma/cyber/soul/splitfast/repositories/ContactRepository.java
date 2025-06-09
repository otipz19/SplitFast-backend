package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

import java.util.Optional;

public interface ContactRepository extends IRepository<ContactEntity, Integer> {

    List<ContactEntity> findAllByUsersAssociationIn(Collection<TwoUsersAssociation> associations);

    @Query("""
        SELECT c
        FROM ContactEntity c
        WHERE (c.usersAssociation.firstUser.id = :userId1 AND c.usersAssociation.secondUser.id = :userId2) OR
              (c.usersAssociation.firstUser.id = :userId2 AND c.usersAssociation.secondUser.id = :userId1)
    """)
    Optional<ContactEntity> findByUsers(@Param("userId1") int userId1, @Param("userId2") int userId2);

}

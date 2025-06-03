package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;

import java.util.Optional;

public interface ActivitiesGroupRepository extends IRepository<ActivitiesGroupEntity, Integer> {

    @Query("""
            SELECT e
            FROM ActivitiesGroupEntity e
            LEFT JOIN FETCH e.members m
            LEFT JOIN FETCH m.user
            WHERE e.id = :id
    """)
    Optional<ActivitiesGroupEntity> findByIdWithFetch(@Param("id") int id);

}

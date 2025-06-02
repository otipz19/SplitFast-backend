package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface ActivitiesGroupMemberRepository extends IRepository<ActivitiesGroupMemberEntity, Integer> {

    @Query("""
            SELECT e.activitiesGroupId
            FROM ActivitiesGroupMemberEntity e
            WHERE e.userId = :userId
    """)
    Set<Integer> findActivitiesGroupIdsByUserId(@Param("userId") int userId);

    Optional<ActivitiesGroupMemberEntity> findByUserAndActivitiesGroup(UserEntity user, ActivitiesGroupEntity activitiesGroup);
}

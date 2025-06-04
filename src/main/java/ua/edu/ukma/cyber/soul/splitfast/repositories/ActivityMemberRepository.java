package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface ActivityMemberRepository extends IRepository<ActivityMemberEntity, Integer> {

    @Query("""
            SELECT e.activityId
            FROM ActivityMemberEntity e
            WHERE e.userId = :userId
    """)
    Set<Integer> findActivityIdsByUserId(@Param("userId") int userId);

    Optional<ActivityMemberEntity> findByUserAndActivityId(UserEntity user, int activityId);
}

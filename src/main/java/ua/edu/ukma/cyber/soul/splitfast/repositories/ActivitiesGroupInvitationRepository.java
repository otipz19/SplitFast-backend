package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupInvitationEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

public interface ActivitiesGroupInvitationRepository extends IRepository<ActivitiesGroupInvitationEntity, Integer> {

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM ActivitiesGroupInvitationEntity
            WHERE usersAssociation.toUserId = :#{#toUser.id} AND activitiesGroupId = :#{#group.id}
        )
    """)
    boolean existsByToUserAndActivitiesGroup(@Param("toUser") UserEntity toUser, @Param("group") ActivitiesGroupEntity group);
}

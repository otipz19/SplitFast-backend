package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;

import java.util.Optional;

public interface ActivitiesGroupMemberRepository extends IRepository<ActivitiesGroupMemberEntity, Integer> {

    Optional<ActivitiesGroupMemberEntity> findByUserIdAndActivitiesGroupId(int userId, int activitiesGroupId);

    void deleteAllByActivitiesGroupId(int activitiesGroupId);
}

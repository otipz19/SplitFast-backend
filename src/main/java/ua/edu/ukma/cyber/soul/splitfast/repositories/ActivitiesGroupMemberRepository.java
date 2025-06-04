package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

import java.util.Optional;

public interface ActivitiesGroupMemberRepository extends IRepository<ActivitiesGroupMemberEntity, Integer> {

    Optional<ActivitiesGroupMemberEntity> findByUserAndActivitiesGroupId(UserEntity user, int activitiesGroupId);

    void deleteAllByActivitiesGroupId(Integer activitiesGroupId);
}

package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

import java.util.Optional;

public interface ActivityMemberRepository extends IRepository<ActivityMemberEntity, Integer> {

    Optional<ActivityMemberEntity> findByUserAndActivityId(UserEntity user, int activityId);

    void deleteAllByActivityId(int activityId);
}

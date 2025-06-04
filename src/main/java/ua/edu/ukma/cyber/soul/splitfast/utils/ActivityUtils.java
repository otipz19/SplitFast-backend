package ua.edu.ukma.cyber.soul.splitfast.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
@RequiredArgsConstructor
public class ActivityUtils {

    private final ActivityMemberRepository activityMemberRepository;
    private final SecurityUtils securityUtils;

    public boolean isCurrentUserMemberOf(ActivityEntity activity) {
        return isUserMemberOf(securityUtils.getCurrentUser(), activity.getId());
    }

    public boolean isUserMemberOf(UserEntity user, ActivityEntity activity) {
        return isUserMemberOf(user, activity.getId());
    }

    public boolean isCurrentUserMemberOf(int activityId) {
        return isUserMemberOf(securityUtils.getCurrentUser(), activityId);
    }

    public boolean isUserMemberOf(UserEntity user, int activityId) {
        return activityMemberRepository.findByUserAndActivityId(user, activityId).isPresent();
    }

    public boolean isCurrentUserOwnerOf(ActivityEntity activity) {
        return isUserOwnerOf(securityUtils.getCurrentUser(), activity.getId());
    }

    public boolean isUserOwnerOf(UserEntity user, ActivityEntity activity) {
        return isUserOwnerOf(user, activity.getId());
    }

    public boolean isCurrentUserOwnerOf(int activityId) {
        return isUserOwnerOf(securityUtils.getCurrentUser(), activityId);
    }

    public boolean isUserOwnerOf(UserEntity user, int activityId) {
        return activityMemberRepository.findByUserAndActivityId(user, activityId)
                .filter(ActivityMemberEntity::isOwner)
                .isPresent();
    }

    public boolean isFinished(ActivityEntity activity) {
        return activity.getTimeFinished() != null;
    }
}

package ua.edu.ukma.cyber.soul.splitfast.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivitiesGroupMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;

@Component
@RequiredArgsConstructor
public class ActivitiesGroupUtils {

    private final ActivitiesGroupMemberRepository activitiesGroupMemberRepository;
    private final SecurityUtils securityUtils;

    public boolean isCurrentUserMemberOf(ActivitiesGroupEntity activitiesGroup) {
        return isUserMemberOf(securityUtils.getCurrentUser(), activitiesGroup.getId());
    }

    public boolean isUserMemberOf(UserEntity user, ActivitiesGroupEntity activitiesGroup) {
        return isUserMemberOf(user, activitiesGroup.getId());
    }

    public boolean isCurrentUserMemberOf(int activitiesGroupId) {
        return isUserMemberOf(securityUtils.getCurrentUser(), activitiesGroupId);
    }

    public boolean isUserMemberOf(UserEntity user, int activitiesGroupId) {
        return activitiesGroupMemberRepository.findByUserAndActivitiesGroupId(user, activitiesGroupId).isPresent();
    }

    public boolean isCurrentUserOwnerOf(ActivitiesGroupEntity activitiesGroup) {
        return isUserOwnerOf(securityUtils.getCurrentUser(), activitiesGroup.getId());
    }

    public boolean isUserOwnerOf(UserEntity user, ActivitiesGroupEntity activitiesGroup) {
        return isUserOwnerOf(user, activitiesGroup.getId());
    }

    public boolean isCurrentUserOwnerOf(int activitiesGroupId) {
        return isUserOwnerOf(securityUtils.getCurrentUser(), activitiesGroupId);
    }

    public boolean isUserOwnerOf(UserEntity user, int activitiesGroupId) {
        return activitiesGroupMemberRepository.findByUserAndActivitiesGroupId(user, activitiesGroupId)
                .filter(ActivitiesGroupMemberEntity::isOwner)
                .isPresent();
    }
}

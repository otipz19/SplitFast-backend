package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;

public interface GeoLabelRepository extends IRepository<GeoLabelEntity, Integer> {

    void deleteAllByActivitiesGroupId(int activitiesGroupId);

    boolean existsByActivitiesGroupIdAndOwnerId(int activitiesGroupId, int userId);
}

package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.GeoLabelEntity;

import java.util.List;

public interface GeoLabelRepository extends IRepository<GeoLabelEntity, Integer> {
    List<GeoLabelEntity> findByName(String name);
}

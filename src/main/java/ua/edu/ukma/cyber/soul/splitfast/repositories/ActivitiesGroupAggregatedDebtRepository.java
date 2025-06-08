package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupAggregatedDebtEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

public interface ActivitiesGroupAggregatedDebtRepository extends IRepository<ActivitiesGroupAggregatedDebtEntity, Integer> {

    List<ActivitiesGroupAggregatedDebtEntity> findAllByUsersAssociationInAndActivitiesGroupId(Collection<TwoUsersAssociation> associations, int activitiesGroupId);
}

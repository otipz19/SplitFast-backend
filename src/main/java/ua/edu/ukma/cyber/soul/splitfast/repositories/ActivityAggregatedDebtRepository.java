package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityAggregatedDebtEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

public interface ActivityAggregatedDebtRepository extends IRepository<ActivityAggregatedDebtEntity, Integer> {

    List<ActivityAggregatedDebtEntity> findAllByUsersAssociationInAndActivityId(Collection<TwoUsersAssociation> associations, int activityId);

    List<ActivityAggregatedDebtEntity> findAllByActivity(ActivityEntity activity);
}

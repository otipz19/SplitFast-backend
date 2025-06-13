package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityAggregatedDebtEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

public interface ActivityAggregatedDebtRepository extends IRepository<ActivityAggregatedDebtEntity, Integer> {

    List<ActivityAggregatedDebtEntity> findAllByUsersAssociationInAndActivityId(Collection<TwoUsersAssociation> associations, int activityId);

    List<ActivityAggregatedDebtEntity> findAllByActivity(ActivityEntity activity);

    @EntityGraph(attributePaths = {"firstUser", "secondUser"})
    List<ActivityAggregatedDebtEntity> findFullByActivityId(int activityId);

    @Query(value = """
        SELECT pg_advisory_xact_lock(-2, :activityId)
    """, nativeQuery = true)
    void lockActivityDebt(@Param("activityId") int activityId);
}

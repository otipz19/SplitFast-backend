package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupAggregatedDebtEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

public interface ActivitiesGroupAggregatedDebtRepository extends IRepository<ActivitiesGroupAggregatedDebtEntity, Integer> {

    List<ActivitiesGroupAggregatedDebtEntity> findAllByUsersAssociationInAndActivitiesGroupId(Collection<TwoUsersAssociation> associations, int activitiesGroupId);

    List<ActivitiesGroupAggregatedDebtEntity> findByActivitiesGroupId(int activitiesGroupId);

    @EntityGraph(attributePaths = {"firstUser", "secondUser"})
    List<ActivitiesGroupAggregatedDebtEntity> findFullByActivitiesGroupId(int activitiesGroupId);

    @Query(value = """
        SELECT pg_advisory_xact_lock(-3, :activitiesGroupId)
    """, nativeQuery = true)
    void lockActivitiesGroupDebt(@Param("activitiesGroupId") int activitiesGroupId);
}

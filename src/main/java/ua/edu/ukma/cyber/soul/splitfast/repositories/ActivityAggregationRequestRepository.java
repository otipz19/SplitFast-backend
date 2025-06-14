package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityAggregationRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivityEntity;

import java.util.List;

public interface ActivityAggregationRequestRepository extends IRepository<ActivityAggregationRequestEntity, Integer> {

    @Query(value = """
        SELECT *
        FROM activity_aggregation_requests
        WHERE activity_id = :activityId
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    ActivityAggregationRequestEntity findForUpdateByActivityId(@Param("activityId") int activityId);

    @Query(value = """
        SELECT a.*
        FROM activity_aggregation_requests r
            JOIN activities a ON r.activity_id = a.id
        FOR UPDATE OF r SKIP LOCKED
    """, nativeQuery = true)
    List<ActivityEntity> findUnprocessedActivities();
}

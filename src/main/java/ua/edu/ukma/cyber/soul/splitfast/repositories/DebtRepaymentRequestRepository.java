package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequestEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersDirectedAssociation;

import java.math.BigDecimal;

public interface DebtRepaymentRequestRepository extends IRepository<DebtRepaymentRequestEntity, Integer> {

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM DebtRepaymentRequestEntity d
            WHERE d.usersAssociation.fromUserId = :#{#association.fromUserId}
                AND d.usersAssociation.toUserId = :#{#association.toUserId}
                AND d.status = :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.DebtRepaymentRequestStatus).PENDING}
        )
    """)
    boolean existsPendingRequestByUsersAssociation(@Param("association") TwoUsersDirectedAssociation association);


    @Query("""
        SELECT COALESCE(
            (
                SELECT SUM(d.amount)
                FROM DebtRepaymentRequestEntity d
                WHERE d.usersAssociation.fromUserId = :#{#association.fromUserId}
                    AND d.usersAssociation.toUserId = :#{#association.toUserId}
                    AND d.status = :#{T(ua.edu.ukma.cyber.soul.splitfast.domain.enums.DebtRepaymentRequestStatus).PENDING}
            ), 0
        )
    """)
    BigDecimal sumPendingRequestsAmount(@Param("association") TwoUsersDirectedAssociation association);
}
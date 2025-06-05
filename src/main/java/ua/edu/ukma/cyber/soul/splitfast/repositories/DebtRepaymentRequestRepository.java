package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest;

import java.math.BigDecimal;

public interface DebtRepaymentRequestRepository extends IRepository<DebtRepaymentRequest, Integer> {
    boolean existsByAssociation_FirstUserIdAndAssociation_SecondUserIdAndSubmittedFalse(Integer fromUserId, Integer toUserId);


    @Query("""
        SELECT COALESCE(SUM(drr.amount), 0)
        FROM DebtRepaymentRequest drr
        WHERE (drr.association.firstUser.id = :fromUserId AND drr.association.secondUser.id = :toUserId)
          AND drr.submitted = FALSE
    """)
    BigDecimal sumUnsubmittedRequests(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);
}
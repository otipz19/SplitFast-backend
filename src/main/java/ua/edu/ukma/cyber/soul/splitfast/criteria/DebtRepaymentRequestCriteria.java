package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest_;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity_;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation_;

import java.time.LocalDateTime;
import java.util.List;

public class DebtRepaymentRequestCriteria extends Criteria<DebtRepaymentRequest, DebtRepaymentRequestCriteriaDto> {

    public DebtRepaymentRequestCriteria(DebtRepaymentRequestCriteriaDto criteriaDto) {
        super(DebtRepaymentRequest.class, criteriaDto);
    }

    @Override
    public List<Predicate> query(Root<DebtRepaymentRequest> root, CriteriaBuilder cb) {
        PredicatesBuilder<DebtRepaymentRequest> builder = new PredicatesBuilder<>(root, cb);

        builder.eq(criteria.getSubmitted(), DebtRepaymentRequest_.submitted);
        builder.between(criteria.getMinAmount(), criteria.getMaxAmount(), DebtRepaymentRequest_.amount);

        LocalDateTime minCreated = criteria.getMinCreatedAt() != null ? criteria.getMinCreatedAt().toLocalDateTime() : null;
        LocalDateTime maxCreated = criteria.getMaxCreatedAt() != null ? criteria.getMaxCreatedAt().toLocalDateTime() : null;
        builder.between(minCreated, maxCreated, DebtRepaymentRequest_.createdAt);

        Integer fromUserId = criteria.getFromUserId();
        Integer toUserId = criteria.getToUserId();

            builder.eq(fromUserId, root.get(DebtRepaymentRequest_.association).get(TwoUsersAssociation_.firstUser).get(UserEntity_.id));
            builder.eq(toUserId, root.get(DebtRepaymentRequest_.association).get(TwoUsersAssociation_.secondUser).get(UserEntity_.id));

        return builder.getPredicates();
    }
}
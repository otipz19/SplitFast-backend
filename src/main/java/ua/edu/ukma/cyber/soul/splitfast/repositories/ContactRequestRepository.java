package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactRequestEntity;

import java.util.List;
import java.util.Optional;

public interface ContactRequestRepository extends IRepository<ContactRequestEntity, Long> {
    List<ContactRequestEntity> findByFromUserIdOrToUserId(Integer fromUserId, Integer toUserId);
    Optional<ContactRequestEntity> findByFromUserIdAndToUserId(Integer fromUserId, Integer toUserId);

}


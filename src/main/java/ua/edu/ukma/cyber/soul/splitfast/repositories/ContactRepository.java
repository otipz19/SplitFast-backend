package ua.edu.ukma.cyber.soul.splitfast.repositories;


import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.ContactId;

import java.util.List;

public interface ContactRepository extends IRepository<ContactEntity, ContactId> {
    List<ContactEntity> findByIdFirstUserIdOrIdSecondUserId(Integer firstUserId, Integer secondUserId);
    boolean existsByIdFirstUserIdAndIdSecondUserId(int firstUserId, int secondUserId);
}

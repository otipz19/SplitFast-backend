package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;

import java.util.Collection;
import java.util.List;

public interface ContactRepository extends IRepository<ContactEntity, Integer> {

    List<ContactEntity> findAllByUsersAssociationIn(Collection<TwoUsersAssociation> associations);
}

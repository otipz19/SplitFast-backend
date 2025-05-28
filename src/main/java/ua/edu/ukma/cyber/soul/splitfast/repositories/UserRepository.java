package ua.edu.ukma.cyber.soul.splitfast.repositories;

import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

public interface UserRepository extends IRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);
}

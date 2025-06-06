package ua.edu.ukma.cyber.soul.splitfast.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity_;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;

import java.util.List;

public class UserCriteria extends Criteria<UserEntity, UserCriteriaDto> {

    private final EnumsMapper enumsMapper;

    public UserCriteria(UserCriteriaDto criteriaDto, EnumsMapper enumsMapper) {
        super(UserEntity.class, criteriaDto);
        this.enumsMapper = enumsMapper;
    }

    @Override
    protected <R> List<Predicate> formPredicates(Root<UserEntity> root, CriteriaQuery<R> query, CriteriaBuilder cb) {
        return new PredicatesBuilder<>(root, cb)
                .in(criteria.getIds(), UserEntity_.id)
                .like(criteria.getQuery(),
                        UserEntity_.username,
                        UserEntity_.name,
                        UserEntity_.email,
                        UserEntity_.phone
                )
                .eq(enumsMapper.map(criteria.getRole()), UserEntity_.role)
                .getPredicates();
    }
}

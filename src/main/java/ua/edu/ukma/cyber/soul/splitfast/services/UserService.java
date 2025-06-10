package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.UserCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.mappers.UserMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.UserValidator;

import java.util.List;

@Service
public class UserService extends BaseCRUDService<UserEntity, CreateUserDto, UpdateUserDto, Integer> {

    private final UserMapper mapper;
    private final EnumsMapper enumsMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserService(IRepository<UserEntity, Integer> repository, CriteriaRepository criteriaRepository,
                       UserValidator validator, IMerger<UserEntity, CreateUserDto, UpdateUserDto> merger, ApplicationEventPublisher eventPublisher,
                       UserMapper mapper, PasswordEncoder passwordEncoder, SecurityUtils securityUtils, EnumsMapper enumsMapper) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, UserEntity.class, UserEntity::new);
        this.mapper = mapper;
        this.enumsMapper = enumsMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @SerializableTransaction(readOnly = true)
    public UserDto getResponseById(int userId) {
        return mapper.toResponse(getById(userId));
    }

    @SerializableTransaction(readOnly = true)
    public UserListDto getListResponseByCriteria(UserCriteriaDto criteriaDto) {
        UserCriteria criteria = new UserCriteria(criteriaDto, enumsMapper);
        List<UserEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @SerializableTransaction(readOnly = true)
    public UserDto getCurrentUser() {
        return mapper.toResponse(securityUtils.getCurrentUser());
    }

    @SerializableTransaction
    public int registerUser(RegisterUserDto registerUserDto) {
        return create(mapper.toCreateUserDto(registerUserDto));
    }

    @SerializableTransaction
    public void updateCurrentUserPassword(UpdatePasswordDto dto) {
        UserEntity user = securityUtils.getCurrentUser();
        ((UserValidator) validator).validForUpdatePassword(user);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash()))
            throw new ValidationException("error.user.update-password.wrong-password");
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);
    }
}

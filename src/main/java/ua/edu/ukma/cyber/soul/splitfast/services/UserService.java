package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.criteria.UserCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
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
public class UserService extends BaseCRUDService<UserEntity, UpdateUserDto, Integer> {

    private final UserMapper mapper;
    private final EnumsMapper enumsMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserService(IRepository<UserEntity, Integer> repository, CriteriaRepository criteriaRepository,
                       UserValidator validator, IMerger<UserEntity, UpdateUserDto> merger, ApplicationEventPublisher eventPublisher,
                       UserMapper mapper, PasswordEncoder passwordEncoder, SecurityUtils securityUtils, EnumsMapper enumsMapper) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, UserEntity.class, UserEntity::new);
        this.mapper = mapper;
        this.enumsMapper = enumsMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public UserDto getResponseById(int userId) {
        return mapper.toResponse(getById(userId));
    }

    @Transactional(readOnly = true)
    public UserListDto getListResponseByCriteria(UserCriteriaDto criteriaDto) {
        UserCriteria criteria = new UserCriteria(criteriaDto, enumsMapper);
        List<UserEntity> entities = getList(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, entities);
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        return mapper.toResponse(securityUtils.getCurrentUser());
    }

    @Transactional
    public int registerUser(RegisterUserDto registerUserDto) {
        return registerUser(UserRole.USER, registerUserDto);
    }

    @Transactional
    public int createUser(CreateUserDto createUserDto) {
        return registerUser(enumsMapper.map(createUserDto.getRole()), createUserDto);
    }

    @Transactional
    public void updateCurrentUserPassword(UpdatePasswordDto dto) {
        UserEntity user = securityUtils.getCurrentUser();
        ((UserValidator) validator).validForUpdatePassword(user);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash()))
            throw new ValidationException("error.user.update-password.wrong-password");
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);
    }

    private int registerUser(UserRole role, RegisterUserDto registerUserDto) {
        UserEntity userEntity = entitySupplier.get();
        userEntity.setRole(role);
        userEntity.setUsername(registerUserDto.getUsername());
        userEntity.setPasswordHash(passwordEncoder.encode(registerUserDto.getPassword()));
        merger.mergeForCreate(userEntity, registerUserDto);
        validator.validForCreate(userEntity);
        return repository.save(userEntity).getId();
    }
}

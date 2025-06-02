package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.RegisterUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.mappers.UserMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.UserValidator;

@Service
public class UserService extends BaseCRUDService<UserEntity, UpdateUserDto, Integer> {

    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserService(IRepository<UserEntity, Integer> repository, CriteriaRepository criteriaRepository,
                       UserValidator validator, IMerger<UserEntity, UpdateUserDto> merger, UserMapper mapper,
                       PasswordEncoder passwordEncoder, SecurityUtils securityUtils) {
        super(repository, criteriaRepository, validator, merger, UserEntity.class, UserEntity::new);
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        return mapper.toResponse(securityUtils.getCurrentUser());
    }

    @Transactional
    public int registerUser(RegisterUserDto registerUserDto) {
        return registerUser(UserRole.USER, registerUserDto);
    }

    private int registerUser(UserRole role, RegisterUserDto registerUserDto) {
        UserEntity userEntity = entitySupplier.get();
        userEntity.setRole(role);
        userEntity.setUsername(registerUserDto.getUsername());
        userEntity.setPasswordHash(passwordEncoder.encode(registerUserDto.getPassword()));
        merger.mergeForCreate(userEntity, registerUserDto);
        ((UserValidator) validator).validForRegister(userEntity);
        return repository.save(userEntity).getId();
    }
}

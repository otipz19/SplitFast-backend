package ua.edu.ukma.cyber.soul.splitfast.services;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.RegisterUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.UserRole;
import ua.edu.ukma.cyber.soul.splitfast.mappers.IMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.UserValidator;

@Service
public class UserService extends BaseCRUDService<UserEntity, UserDto, UpdateUserDto, Integer> {

    private final PasswordEncoder passwordEncoder;

    public UserService(IRepository<UserEntity, Integer> repository, UserValidator validator,
                       IMerger<UserEntity, UpdateUserDto> merger, IMapper<UserEntity, UserDto> mapper,
                       PasswordEncoder passwordEncoder) {
        super(repository, validator, merger, mapper, UserEntity.class, UserEntity::new);
        this.passwordEncoder = passwordEncoder;
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

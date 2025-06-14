package ua.edu.ukma.cyber.soul.splitfast.validators;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;

@Component
public class UserAvatarValidator extends BaseFileValidator<UserEntity> {

    public UserAvatarValidator(IRepository<UserEntity, Integer> repository, IValidator<UserEntity> validator) {
        super(repository, validator, "image/.{1,127}");
    }

    @Override
    public FileType supportedFileType() {
        return FileType.USER_AVATAR;
    }
}

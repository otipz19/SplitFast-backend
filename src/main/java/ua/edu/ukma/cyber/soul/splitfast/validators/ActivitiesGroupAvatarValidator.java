package ua.edu.ukma.cyber.soul.splitfast.validators;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.FileInfoEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;

import java.util.List;

@Component
public class ActivitiesGroupAvatarValidator extends BaseFileValidator<ActivitiesGroupEntity> {

    public ActivitiesGroupAvatarValidator(IRepository<ActivitiesGroupEntity, Integer> repository, IValidator<ActivitiesGroupEntity> validator) {
        super(repository, validator, "image/.{1,127}");
    }

    @Override
    public void validForView(FileInfoEntity fileInfo) {}

    @Override
    public void validForView(List<FileInfoEntity> fileInfos) {}

    @Override
    public FileType supportedFileType() {
        return FileType.ACTIVITIES_GROUP_AVATAR;
    }
}

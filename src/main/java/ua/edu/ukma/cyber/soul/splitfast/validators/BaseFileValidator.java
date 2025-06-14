package ua.edu.ukma.cyber.soul.splitfast.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.FileInfoEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class BaseFileValidator<E> implements IFileValidator {

    protected final IRepository<E, Integer> repository;
    protected final IValidator<E> validator;
    protected final Pattern typePattern;

    public BaseFileValidator(IRepository<E, Integer> repository, IValidator<E> validator, String typePattern) {
        this.repository = repository;
        this.validator = validator;
        this.typePattern = Pattern.compile(typePattern);
    }

    @Override
    public void validForView(FileInfoEntity fileInfo) {
        validator.validForView(getBoundEntity(fileInfo));
    }

    @Override
    public void validForView(List<FileInfoEntity> fileInfos) {
        validator.validForView(getBoundEntities(fileInfos));
    }

    @Override
    public void validForUpload(MultipartFile file, FileType fileType, int boundEntityId) {
        if (file == null || file.getSize() == 0)
            throw new ValidationException("error.file.empty");
        if (!typePattern.matcher(Objects.requireNonNullElse(file.getContentType(), StringUtils.EMPTY)).matches())
            throw new ValidationException("error.file.invalid-content-type");
        validator.validForUpdate(getBoundEntity(boundEntityId));
    }

    @Override
    public void validForDelete(FileInfoEntity fileInfo) {
        validator.validForUpdate(getBoundEntity(fileInfo));
    }

    private E getBoundEntity(FileInfoEntity fileInfo) {
        return getBoundEntity(fileInfo.getBoundEntityId());
    }

    private E getBoundEntity(int boundEntityId) {
        return repository.findById(boundEntityId).orElseThrow(ForbiddenException::new);
    }

    private List<E> getBoundEntities(List<FileInfoEntity> fileInfos) {
        return repository.findAllById(fileInfos.stream().map(FileInfoEntity::getBoundEntityId).toList());
    }
}

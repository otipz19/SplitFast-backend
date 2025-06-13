package ua.edu.ukma.cyber.soul.splitfast.validators;

import org.springframework.web.multipart.MultipartFile;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.FileInfoEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;

import java.util.List;

public interface IFileValidator {

    FileType supportedFileType();

    void validForView(FileInfoEntity fileInfo);

    void validForView(List<FileInfoEntity> fileInfo);

    void validForUpload(MultipartFile file, FileType fileType, int boundEntityId);

    void validForDelete(FileInfoEntity fileInfo);
}

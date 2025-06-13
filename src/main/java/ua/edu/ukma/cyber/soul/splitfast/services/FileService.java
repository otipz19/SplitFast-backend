package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.ukma.cyber.soul.splitfast.annotations.SerializableTransaction;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.EntityFilesInfoDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.FileTypeDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.FileInfoEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.FileInfoRepository;
import ua.edu.ukma.cyber.soul.splitfast.utils.ResourceInfo;
import ua.edu.ukma.cyber.soul.splitfast.validators.IFileValidator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FileService {

    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    private final StorageService storageService;
    private final FileInfoRepository fileInfoRepository;
    private final EnumsMapper enumsMapper;

    private final Map<Class<?>, FileType> fileTypesMap;
    private final Map<FileType, IFileValidator> fileValidatorsMap;

    public FileService(StorageService storageService, FileInfoRepository fileInfoRepository, EnumsMapper enumsMapper, Collection<IFileValidator> fileValidators) {
        this.storageService = storageService;
        this.fileInfoRepository = fileInfoRepository;
        this.enumsMapper = enumsMapper;
        this.fileTypesMap = Arrays.stream(FileType.values()).collect(Collectors.toMap(FileType::getEntityClass, Function.identity()));
        this.fileValidatorsMap = fileValidators.stream().collect(Collectors.toMap(IFileValidator::supportedFileType, Function.identity()));
    }

    @SneakyThrows
    @SerializableTransaction(readOnly = true)
    public ResourceInfo getFile(UUID id) {
        FileInfoEntity fileInfo = getFileInfo(id);
        fileValidatorsMap.get(fileInfo.getFileType()).validForView(fileInfo);
        Resource file = storageService.getFile(id);
        return new ResourceInfo(file, fileInfo.getMimeType(), file.lastModified());
    }

    @SerializableTransaction(readOnly = true)
    public List<EntityFilesInfoDto> getEntitiesFilesInfo(FileTypeDto fileTypeDto, List<Integer> entitiesIds) {
        FileType fileType = enumsMapper.map(fileTypeDto);
        List<FileInfoEntity> fileInfos = fileInfoRepository.findAllByFileTypeAndBoundEntityIdIn(fileType, entitiesIds);
        fileValidatorsMap.get(fileType).validForView(fileInfos);
        Map<Integer, List<UUID>> idsMap = computeIdsMap(fileInfos);
        return entitiesIds.stream()
                .map(i -> new EntityFilesInfoDto(i, idsMap.getOrDefault(i, List.of())))
                .toList();
    }

    private Map<Integer, List<UUID>> computeIdsMap(List<FileInfoEntity> fileInfos) {
        return fileInfos.stream()
                .collect(
                        Collectors.groupingBy(
                                FileInfoEntity::getBoundEntityId,
                                Collectors.mapping(FileInfoEntity::getId, Collectors.toList())
                        )
                );
    }

    @SneakyThrows
    @SerializableTransaction
    public UUID uploadFile(MultipartFile file, FileTypeDto fileTypeDto, int boundEntityId) {
        FileType fileType = enumsMapper.map(fileTypeDto);
        fileValidatorsMap.get(fileType).validForUpload(file, fileType, boundEntityId);
        FileInfoEntity fileInfo = FileInfoEntity.builder()
                .fileType(fileType)
                .boundEntityId(boundEntityId)
                .mimeType(StringUtils.defaultIfBlank(file.getContentType(), DEFAULT_MIME_TYPE))
                .build();
        if (fileType.isUnique())
            fileInfoRepository.deleteByFileTypeAndBoundEntityId(fileType, boundEntityId).forEach(storageService::deleteFile);
        UUID id = fileInfoRepository.save(fileInfo).getId();
        storageService.saveFile(id, file.getBytes());
        return id;
    }

    @SerializableTransaction
    public void deleteFile(UUID id) {
        FileInfoEntity fileInfo = getFileInfo(id);
        fileValidatorsMap.get(fileInfo.getFileType()).validForDelete(fileInfo);
        fileInfoRepository.deleteById(id);
        storageService.deleteFile(id);
    }

    @EventListener
    public void clearFiles(DeleteEntityEvent<? extends IGettableById<Integer>, Integer> event) {
        FileType fileType = getFileType(event.getEntity().getClass());
        if (fileType == null) return;
        fileInfoRepository.deleteByFileTypeAndBoundEntityId(fileType, event.getId()).forEach(storageService::deleteFile);
    }

    private FileType getFileType(Class<?> entityClass) {
        while (entityClass != null) {
            FileType fileType = fileTypesMap.get(entityClass);
            if (fileType != null)
                return fileType;
            entityClass = entityClass.getSuperclass();
        }
        return null;
    }

    private FileInfoEntity getFileInfo(UUID id) {
        return fileInfoRepository.findById(id).orElseThrow(() -> new NotFoundException(FileInfoEntity.class, "id: " + id));
    }
}

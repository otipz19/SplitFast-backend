package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StorageService {

    private static final String FILES_DIRECTORY = "files";

    private final String storageRoot;

    public StorageService(@Value("${storage.root}") String storageRoot) {
        this.storageRoot = storageRoot;
    }

    public Resource getFile(UUID fileId) {
        Path path = filePath(fileId);
        if (Files.notExists(path))
            throw new NotFoundException();
        return new FileSystemResource(path);
    }

    public void saveFile(UUID fileId, byte[] file) {
        saveFile(filePath(fileId), file);
    }

    public void deleteFile(UUID fileId) {
        deleteFile(filePath(fileId));
    }

    private Path filePath(UUID fileId) {
        return Paths.get(path(storageRoot, FILES_DIRECTORY, fileId));
    }

    @SneakyThrows
    private void saveFile(final Path filePath, final byte[] file) {
        log.info("Saving file under path {}", filePath);
        if (Files.notExists(filePath))
            Files.createDirectories(filePath.getParent());
        Files.write(filePath, file, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @SneakyThrows
    private void deleteFile(final Path filePath) {
        log.info("Deleting file under path {}", filePath);
        Files.deleteIfExists(filePath);
    }

    private String path(final Object... pathComponents) {
        return Arrays.stream(pathComponents)
            .map(Object::toString)
            .map(String::toLowerCase)
            .collect(Collectors.joining(File.separator));
    }

}

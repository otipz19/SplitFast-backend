package ua.edu.ukma.cyber.soul.splitfast.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.FileInfoEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FileInfoRepository extends IRepository<FileInfoEntity, UUID> {

    List<FileInfoEntity> findAllByFileTypeAndBoundEntityIdIn(FileType fileType, Collection<Integer> boundEntitiesIds);

    @Query(value = """
        DELETE FROM files_info
        WHERE file_type = :#{#fileType.name()} AND bound_entity_id = :boundEntityId
        RETURNING id
    """, nativeQuery = true)
    // do not use @Modifying because of the RETURNING clause
    List<UUID> deleteByFileTypeAndBoundEntityId(@Param("fileType") FileType fileType, @Param("boundEntityId") int boundEntityId);
}

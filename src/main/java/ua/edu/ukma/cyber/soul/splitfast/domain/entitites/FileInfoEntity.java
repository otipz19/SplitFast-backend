package ua.edu.ukma.cyber.soul.splitfast.domain.entitites;

import jakarta.persistence.*;
import lombok.*;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.IGettableById;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files_info")
public class FileInfoEntity implements IGettableById<UUID> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @Column(name = "bound_entity_id", nullable = false)
    private int boundEntityId;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;
}

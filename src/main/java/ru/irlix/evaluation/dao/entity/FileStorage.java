package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "file_storage")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "doc_type")
    private String docType;

    @ManyToOne
    @JoinColumn(name = "estimation")
    private Estimation estimation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder")
    private Folder folder;

}

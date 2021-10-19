package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.FileStorage;
import ru.irlix.evaluation.dao.entity.Folder;

import java.util.List;
import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    List<FileStorage> findAllByEstimation(Estimation estimation);

    List<FileStorage> findAllByEstimationAndFolder(Estimation estimation, Folder folder);

    List<FileStorage> findByIdIn(List<Long> idList);

    List<FileStorage> findAllByUuid(UUID uuid);
}

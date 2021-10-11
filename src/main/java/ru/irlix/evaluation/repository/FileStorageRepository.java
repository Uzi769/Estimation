package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.FileStorage;
import ru.irlix.evaluation.dao.entity.Folder;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    List<FileStorage> findAllByEstimation(Estimation estimation);

    List<FileStorage> findAllByEstimationAndFolder(Estimation estimation, Folder folder);
}

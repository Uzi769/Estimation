package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByValue(String value);
}

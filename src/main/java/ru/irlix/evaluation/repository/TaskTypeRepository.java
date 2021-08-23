package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;

public interface TaskTypeRepository extends JpaRepository<TaskTypeDictionary, Long> {
}

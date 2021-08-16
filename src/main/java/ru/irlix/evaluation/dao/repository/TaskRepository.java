package ru.irlix.evaluation.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsTaskById(Long id);

}

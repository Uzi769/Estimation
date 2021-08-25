package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(value = "task.tasks")
    List<Task> findByPhaseId(Long phaseId);
}

package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @NonNull
    @EntityGraph(value = "task.tasks", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Task> findById(@NonNull Long id);

    @EntityGraph(value = "task.tasks", type = EntityGraph.EntityGraphType.LOAD)
    List<Task> findByPhaseId(Long phaseId);
}
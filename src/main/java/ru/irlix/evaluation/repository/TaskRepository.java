package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> { }

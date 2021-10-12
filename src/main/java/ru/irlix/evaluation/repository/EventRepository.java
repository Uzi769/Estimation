package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

}

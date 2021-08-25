package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Phase;

import java.util.List;
import java.util.Optional;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    @EntityGraph(value = "phase.tasks")
    @NonNull
    Optional<Phase> findById(@NonNull Long id);

    @EntityGraph("phase.tasks")
    List<Phase> findByEstimationIdInOrderById(List<Long> ids);
}

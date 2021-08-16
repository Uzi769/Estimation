package ru.irlix.evaluation.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Phase;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    boolean existsPhaseById(Long id);

}

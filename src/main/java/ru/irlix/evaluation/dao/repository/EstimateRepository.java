package ru.irlix.evaluation.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    boolean existsEstimateById(Long id);

}

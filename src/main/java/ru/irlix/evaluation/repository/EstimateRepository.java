package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Estimation;

public interface EstimateRepository extends JpaRepository<Estimation, Long> {

    boolean existsEstimateById(Long id);

}

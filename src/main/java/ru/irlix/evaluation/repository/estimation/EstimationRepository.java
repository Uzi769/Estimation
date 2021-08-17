package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.irlix.evaluation.dao.entity.Estimation;

public interface EstimationRepository extends PagingAndSortingRepository<Estimation, Long>, EstimationFilterRepository { }

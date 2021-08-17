package ru.irlix.evaluation.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.irlix.evaluation.dao.entity.Estimation;

public interface EstimationRepository extends PagingAndSortingRepository<Estimation, Long>, EstimationFilterRepository { }

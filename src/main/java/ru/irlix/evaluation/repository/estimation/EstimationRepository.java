package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.util.List;
import java.util.Optional;

public interface EstimationRepository extends PagingAndSortingRepository<Estimation, Long>, EstimationFilterRepository {

    @EntityGraph(value = "estimation.phases")
    @NonNull
    Optional<Estimation> findById(@NonNull Long id);
}

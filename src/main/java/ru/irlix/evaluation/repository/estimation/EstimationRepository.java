package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.util.Optional;

public interface EstimationRepository extends PagingAndSortingRepository<Estimation, Long>, EstimationFilterRepository {

    @Query("select e from Estimation e join fetch e.phases p join fetch p.tasks t where t.parent is null")
    @NonNull
    Optional<Estimation> findById(@NonNull Long id);
}

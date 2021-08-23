package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.util.Optional;

public interface EstimationRepository extends PagingAndSortingRepository<Estimation, Long>, EstimationFilterRepository {

    @Query("select distinct e" +
            " from Estimation e " +
            " left join fetch e.phases p " +
            " left join fetch p.tasks t " +
            " where t.parent is null and e.id = ?1")
    @NonNull
    Optional<Estimation> findById(@NonNull Long id);
}

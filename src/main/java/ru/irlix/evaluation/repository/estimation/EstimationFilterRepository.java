package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.jpa.repository.Query;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.util.List;

public interface EstimationFilterRepository {

    @Query("select e from Estimation e join fetch e.phases p join fetch p.tasks t where t.parent is null")
    List<Estimation> filter(EstimationFilterRequest request);
}

package ru.irlix.evaluation.repository.estimation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dao.entity.Estimation;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EstimationFilterRepositoryImpl implements EstimationFilterRepository {

    private final EntityManager manager;

    @Override
    public List<Estimation> filter(EstimationFilterRequest request) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estimation> query = builder.createQuery(Estimation.class);
        Root<Estimation> root = query.from(Estimation.class);

        int offset = request.getPage() * request.getSize();

        List<Predicate> filterPredicates = new ArrayList<>();

        if (request.getName() != null && !request.getName().isEmpty()) {
            filterPredicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
        }

        if (request.getClient() != null && !request.getClient().isEmpty()) {
            filterPredicates.add(builder.like(root.get("client"), "%" + request.getClient() + "%"));
        }

        if (request.getStatus() != null) {
            filterPredicates.add(builder.equal(root.get("status").get("id"), request.getStatus()));
        }

        if (request.getBeginDate() != null) {
            filterPredicates.add(builder.greaterThanOrEqualTo(root.get("createDate"), request.getBeginDate()));
        }

        if (request.getEndDate() != null) {
            filterPredicates.add(builder.lessThanOrEqualTo(root.get("createDate"), request.getEndDate()));
        }

        query.select(root).where(builder.and(filterPredicates.toArray(new Predicate[0])));
        TypedQuery<Estimation> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(request.getSize());

        return typedQuery.getResultList();
    }
}

package ru.irlix.evaluation.repository.estimation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;

import javax.persistence.EntityGraph;
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
    private CriteriaBuilder builder;
    private Root<Estimation> root;

    @Override
    public Page<Estimation> filter(EstimationFilterRequest request) {
        builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estimation> query = builder.createQuery(Estimation.class);
        root = query.from(Estimation.class);

        int offset = request.getPage() * request.getSize();
        EntityGraph<?> graph = manager.getEntityGraph("estimation.phases");

        Predicate filterPredicates = getFilterPredicate(request);

        query.select(root)
                .where(filterPredicates)
                .orderBy(builder.asc(root.get("createDate")));

        TypedQuery<Estimation> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(offset)
                .setMaxResults(request.getSize())
                .setHint("javax.persistence.fetchgraph", graph);

        return new PageImpl<>(
                typedQuery.getResultList(),
                PageRequest.of(request.getPage(), request.getSize()),
                getTotalCount(filterPredicates)
        );
    }

    private Predicate getFilterPredicate(EstimationFilterRequest request) {
        List<Predicate> filterPredicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(request.getName())) {
            filterPredicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
        }

        if (StringUtils.isNotEmpty(request.getClient())) {
            filterPredicates.add(builder.like(root.get("client"), "%" + request.getClient() + "%"));
        }

        if (StringUtils.isNotEmpty(request.getCreator())) {
            filterPredicates.add(builder.like(root.get("creator"), "%" + request.getCreator() + "%"));
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

        return builder.and(filterPredicates.toArray(new Predicate[0]));
    }

    private Long getTotalCount(Predicate filterPredicate) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Estimation.class)));
        countQuery.where(filterPredicate);

        return manager.createQuery(countQuery).getSingleResult();
    }
}

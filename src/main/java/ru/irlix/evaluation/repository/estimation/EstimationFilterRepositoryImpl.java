package ru.irlix.evaluation.repository.estimation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.PageableAndSortableRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EstimationFilterRepositoryImpl implements EstimationFilterRepository {

    private final EntityManager manager;
    private CriteriaBuilder builder;
    private CriteriaQuery<Estimation> query;
    private Root<Estimation> root;

    @Override
    public Page<Estimation> filter(EstimationFilterRequest request) {
        builder = manager.getCriteriaBuilder();
        query = builder.createQuery(Estimation.class);
        root = query.from(Estimation.class);

        return findPageableEstimations(request);
    }

    private Page<Estimation> findPageableEstimations(EstimationFilterRequest request) {
        Pageable pageable = getPageable(request);
        int offset = pageable.getPageNumber() * pageable.getPageSize();

        query.select(root)
                .where(getPredicate(request))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        TypedQuery<Estimation> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(
                typedQuery.getResultList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                getTotalCount(request)
        );
    }

    private Predicate getPredicate(EstimationFilterRequest request) {
        List<Predicate> filterPredicates = new ArrayList<>();

        if (request.isDeleted()) {
            filterPredicates.add(builder.isNotNull(root.get("deleteDate")));
        } else {
            filterPredicates.add(builder.isNull(root.get("deleteDate")));
        }

        if (StringUtils.isNotEmpty(request.getText())) {
            String pattern = "%" + request.getText().toLowerCase() + "%";
            List<Predicate> textPredicates = List.of(
                    builder.like(builder.lower(root.get("name")), pattern),
                    builder.like(builder.lower(root.get("client")), pattern),
                    builder.like(builder.lower(root.get("creator")), pattern)
            );

            filterPredicates.add(builder.or(textPredicates.toArray(new Predicate[0])));
        }

        if (StringUtils.isNotEmpty(request.getName())) {
            String pattern = "%" + request.getName().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("name")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getClient())) {
            String pattern = "%" + request.getClient().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("client")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getCreator())) {
            String pattern = "%" + request.getCreator().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("creator")), pattern));
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

        if (request.getUserId() != null) {
            Join<Estimation, User> usersJoin = root.join("users", JoinType.LEFT);
            filterPredicates.add(builder.equal(usersJoin.get("userId"), request.getUserId()));
        }

        return builder.and(filterPredicates.toArray(new Predicate[0]));
    }

    private Long getTotalCount(EstimationFilterRequest request) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        root = countQuery.from(Estimation.class);
        countQuery.select(builder.count(root));
        countQuery.where(getPredicate(request));

        return manager.createQuery(countQuery).getSingleResult();
    }

    private Pageable getPageable(PageableAndSortableRequest request) {
        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                getSort(request),
                getSortFields(request)
        );
    }

    private String getSortFields(PageableAndSortableRequest request) {
        return request.getNameSortField() == null ? "createDate" : request.getNameSortField();
    }

    private Sort.Direction getSort(PageableAndSortableRequest request) {
        if (request.getSortAsc() == null) {
            return Sort.Direction.DESC;
        }

        return request.getSortAsc()
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }
}

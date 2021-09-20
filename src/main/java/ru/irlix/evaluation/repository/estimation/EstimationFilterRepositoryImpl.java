package ru.irlix.evaluation.repository.estimation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationFindAnyRequest;
import ru.irlix.evaluation.dto.request.EstimationPageRequest;

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

        return findPageableEstimations(getPageable(request), getPredicate(request));
    }

    @Override
    public Page<Estimation> findAny(EstimationFindAnyRequest request) {
        builder = manager.getCriteriaBuilder();
        query = builder.createQuery(Estimation.class);
        root = query.from(Estimation.class);

        return findPageableEstimations(getPageable(request), getPredicate(request));
    }

    private Pageable getPageable(EstimationPageRequest request) {
        if (request.getNameSortField() != null && request.getSortAsc() != null) {
            return PageRequest.of(request.getPage(),
                    request.getSize(),
                    request.getSortAsc() ? Sort.Direction.ASC : Sort.Direction.DESC,
                    request.getNameSortField());
        } else {
            return PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createDate");
        }
    }

    private Page<Estimation> findPageableEstimations(Pageable pageable, Predicate predicate) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();

        query.select(root)
                .where(predicate)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        TypedQuery<Estimation> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(
                typedQuery.getResultList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                getTotalCount(predicate)
        );
    }

    private Predicate getPredicate(EstimationFilterRequest request) {
        List<Predicate> filterPredicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(request.getName())) {
            filterPredicates.add(builder.like(builder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
        }

        if (StringUtils.isNotEmpty(request.getClient())) {
            filterPredicates.add(builder.like(builder.lower(root.get("client")), "%" + request.getClient().toLowerCase() + "%"));
        }

        if (StringUtils.isNotEmpty(request.getCreator())) {
            filterPredicates.add(builder.like(builder.lower(root.get("creator")), "%" + request.getCreator().toLowerCase() + "%"));
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
            Fetch<Estimation, User> usersFetch = root.fetch("users", JoinType.LEFT);
            Join<Estimation, User> usersJoin = (Join<Estimation, User>) usersFetch;
            filterPredicates.add(builder.equal(usersJoin.get("userId"), request.getUserId()));
        }

        return builder.and(filterPredicates.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(EstimationFindAnyRequest request) {
        List<Predicate> textPredicates = new ArrayList<>();
        List<Predicate> otherPredicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(request.getText())) {
            textPredicates.add(builder.like(builder.lower(root.get("name")), "%" + request.getText().toLowerCase() + "%"));
            textPredicates.add(builder.like(builder.lower(root.get("client")), "%" + request.getText().toLowerCase() + "%"));
            textPredicates.add(builder.like(builder.lower(root.get("creator")), "%" + request.getText().toLowerCase() + "%"));
        }

        if (request.getStatus() != null) {
            otherPredicates.add(builder.equal(root.get("status").get("id"), request.getStatus()));
        }

        if (request.getBeginDate() != null) {
            otherPredicates.add(builder.greaterThanOrEqualTo(root.get("createDate"), request.getBeginDate()));
        }

        if (request.getEndDate() != null) {
            otherPredicates.add(builder.lessThanOrEqualTo(root.get("createDate"), request.getEndDate()));
        }

        if (request.getUserId() != null) {
            Fetch<Estimation, User> usersFetch = root.fetch("users", JoinType.LEFT);
            Join<Estimation, User> usersJoin = (Join<Estimation, User>) usersFetch;
            otherPredicates.add(builder.equal(usersJoin.get("userId"), request.getUserId()));
        }

        return builder.and(
                textPredicates.isEmpty() ? builder.and() : builder.or(textPredicates.toArray(new Predicate[0])),
                builder.and(otherPredicates.toArray(new Predicate[0]))
        );
    }

    private Long getTotalCount(Predicate filterPredicate) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Estimation.class)));
        countQuery.where(filterPredicate);

        return manager.createQuery(countQuery).getSingleResult();
    }
}

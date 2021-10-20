package ru.irlix.evaluation.repository.event;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dto.request.EventFilterRequest;
import ru.irlix.evaluation.dto.request.PageableAndSortableRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EventFilterRepositoryImpl implements EventFilterRepository {

    private final EntityManager manager;
    private CriteriaBuilder builder;
    private Root<Event> root;

    @Override
    public Page<Event> filter(EventFilterRequest request) {
        builder = manager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        root = query.from(Event.class);

        Pageable pageable = getPageable(request);
        int offset = pageable.getPageNumber() * pageable.getPageSize();

        query.select(root)
                .where(getPredicate(request))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        TypedQuery<Event> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(
                typedQuery.getResultList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                getTotalCount(request)
        );
    }

    private Predicate getPredicate(EventFilterRequest request) {
        List<Predicate> filterPredicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(request.getText())) {
            String pattern = "%" + request.getText().toLowerCase() + "%";
            List<Predicate> textPredicates = List.of(
                    builder.like(builder.lower(root.get("username")), pattern),
                    builder.like(builder.lower(root.get("description")), pattern),
                    builder.like(builder.lower(root.get("estimationName")), pattern),
                    builder.like(builder.lower(root.get("phaseName")), pattern),
                    builder.like(builder.lower(root.get("taskName")), pattern)
            );

            filterPredicates.add(builder.or(textPredicates.toArray(new Predicate[0])));
        }

        if (StringUtils.isNotEmpty(request.getUsername())) {
            String pattern = "%" + request.getUsername().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("username")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getDescription())) {
            String pattern = "%" + request.getDescription().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("description")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getEstimationName())) {
            String pattern = "%" + request.getEstimationName().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("estimationName")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getPhaseName())) {
            String pattern = "%" + request.getPhaseName().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("phaseName")), pattern));
        }

        if (StringUtils.isNotEmpty(request.getTaskName())) {
            String pattern = "%" + request.getTaskName().toLowerCase() + "%";
            filterPredicates.add(builder.like(builder.lower(root.get("taskName")), pattern));
        }

        if (request.getEstimationId() != null) {
            filterPredicates.add(builder.equal(root.get("estimationId"), request.getEstimationId()));
        }

        if (request.getPhaseId() != null) {
            filterPredicates.add(builder.equal(root.get("phaseId"), request.getPhaseId()));
        }

        if (request.getTaskId() != null) {
            filterPredicates.add(builder.equal(root.get("taskId"), request.getTaskId()));
        }

        if (request.getActionId() != null) {
            filterPredicates.add(builder.equal(root.get("action").get("id"), request.getActionId()));
        }

        if (request.getBeginDate() != null) {
            filterPredicates.add(builder.greaterThanOrEqualTo(root.get("date"), request.getBeginDate()));
        }

        if (request.getEndDate() != null) {
            filterPredicates.add(builder.lessThanOrEqualTo(root.get("date"), request.getEndDate()));
        }

        return builder.and(filterPredicates.toArray(new Predicate[0]));
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
        return request.getNameSortField() == null ? "date" : request.getNameSortField();
    }

    private Sort.Direction getSort(PageableAndSortableRequest request) {
        if (request.getSortAsc() == null) {
            return Sort.Direction.DESC;
        }

        return request.getSortAsc()
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    private Long getTotalCount(EventFilterRequest request) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        root = countQuery.from(Event.class);
        countQuery.select(builder.count(root));
        countQuery.where(getPredicate(request));

        return manager.createQuery(countQuery).getSingleResult();
    }
}

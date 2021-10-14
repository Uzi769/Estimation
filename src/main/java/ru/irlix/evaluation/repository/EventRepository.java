package ru.irlix.evaluation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.irlix.evaluation.dao.entity.Event;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    Page<Event> findAllByOrderByIdDesc(Pageable pageable);
}

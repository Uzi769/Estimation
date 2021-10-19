package ru.irlix.evaluation.repository.event;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.irlix.evaluation.dao.entity.Event;

public interface EventRepository extends PagingAndSortingRepository<Event, Long>, EventFilterRepository { }

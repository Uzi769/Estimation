package ru.irlix.evaluation.repository.event;

import org.springframework.data.domain.Page;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dto.request.EventFilterRequest;

public interface EventFilterRepository {

    Page<Event> filter(EventFilterRequest request);
}

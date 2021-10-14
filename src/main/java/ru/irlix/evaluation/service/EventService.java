package ru.irlix.evaluation.service;

import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.irlix.evaluation.dto.response.EventResponse;

public interface EventService {

    Page<EventResponse> getAllEvents(Pageable pageable);

    void createEvent(JoinPoint joinPoint, Object value);

    Object getElementToDelete(JoinPoint joinPoint);
}



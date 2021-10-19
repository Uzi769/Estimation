package ru.irlix.evaluation.service;

import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import ru.irlix.evaluation.dto.request.EventFilterRequest;
import ru.irlix.evaluation.dto.response.EventResponse;

public interface EventService {

    Page<EventResponse> getAllEvents(EventFilterRequest request);

    void createEvent(JoinPoint joinPoint, Object value);

    Object getElementToDelete(JoinPoint joinPoint);
}



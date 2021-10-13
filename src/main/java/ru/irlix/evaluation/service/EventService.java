package ru.irlix.evaluation.service;

import org.aspectj.lang.JoinPoint;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getAllEvents();

    void createEvent(JoinPoint joinPoint, Object value);

    void deleteEvent(Event eventMediator, String methodName, JoinPoint joinPoint);
}



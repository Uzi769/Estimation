package ru.irlix.evaluation.service;

import org.aspectj.lang.JoinPoint;
import ru.irlix.evaluation.dto.response.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> getAllEvents();

    void createEvent(JoinPoint joinPoint, Object value);

    Object getElementToDelete(JoinPoint joinPoint);
}



package ru.irlix.evaluation.service;

import org.aspectj.lang.JoinPoint;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dto.response.EventResponse;

import java.time.Instant;
import java.util.List;

public interface EventService {
    List<EventResponse> getAllEvents();
    Instant getEventCreationDate();

    void createEvent(JoinPoint joinPoint, String methodName);
    void deleteEvent(Event eventMediator, String methodName, JoinPoint joinPoint);
    Event saveTheEventBeforeDeletingEstimation(Long estimationId);
    Event saveTheEventBeforeDeletingPhase(Long phaseId);
    Event saveTheEventBeforeDeletingTask(Long phaseId);


}



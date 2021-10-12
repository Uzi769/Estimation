package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dao.helper.TaskHelper;
import ru.irlix.evaluation.dao.mapper.EventMapper;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.EventRepository;
import ru.irlix.evaluation.service.EventService;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper mapper;

    private final EstimationHelper estimationHelper;

    private final PhaseHelper phaseHelper;

    private final TaskHelper taskHelper;

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(mapper::EventToEventResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Event saveTheEventBeforeDeletingEstimation(Long estimationId) {
        Estimation estimation = estimationHelper.findEstimationById(estimationId);
        return mapper.EstimationToEvent(estimation);
    }

    @Transactional
    @Override
    public Event saveTheEventBeforeDeletingPhase(Long phaseId) {
        Phase phase = phaseHelper.findPhaseById(phaseId);
        return mapper.PhaseToEvent(phase);
    }

    @Override
    public Instant getEventCreationDate() {
        return Instant.now();
    }

    @Override
    public Event saveTheEventBeforeDeletingTask(Long phaseId) {
        Task task = taskHelper.findTaskById(phaseId);
        return mapper.TaskToEvent(task);
    }

    @Transactional
    @Override
    public void createEvent(JoinPoint joinPoint, String methodName) {
        Event event;
        Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
        if (objectEvent != null) {
            switch (methodName) {
                case "createEstimation":
                    EstimationRequest estimationRequest = (EstimationRequest) objectEvent;
                    //Uzi переделать по id
                    Estimation estimation = estimationHelper.findEstimationByName(estimationRequest.getName());
                    event = mapper.EstimationToEvent(estimation);
                    event.setValue("Создана оценка");
                    eventRepository.save(event);
                    break;
                case "createPhase":
                    PhaseRequest phaseRequest = (PhaseRequest) objectEvent;
                    //Uzi переделать по id
                    Phase phase = phaseHelper.findPhaseByName(phaseRequest.getName());
                    event = mapper.PhaseToEvent(phase);
                    event.setValue("Создана фаза");
                    eventRepository.save(event);
                    break;
                case "createTask":
                    TaskRequest taskRequest = (TaskRequest) objectEvent;
                    //Uzi переделать по id
                    Task task = taskHelper.findTaskByName(taskRequest.getName());
                    event = mapper.TaskToEvent(task);
                    if (task.getType().getId() == 1) {
                        event.setValue("Создана фича");
                    } else if (task.getType().getId() == 2) {
                        event.setValue("Создана задача");
                    }
                    eventRepository.save(event);
                    break;
            }
        }

    }

    @Override
    public void deleteEvent(Event eventMediator, String methodName, JoinPoint joinPoint) {
        Event event = new Event();
        event.setUserName(eventMediator.getUserName());
        event.setEstimationName(eventMediator.getEstimationName());
        event.setUser(eventMediator.getUser());
        event.setDate(eventMediator.getDate());

        switch (methodName) {
            case "deleteEstimation":
                event.setValue("Удалена оценка");
                break;
            case "deletePhase":
                event.setValue("Удалена фаза");
                event.setPhaseName(eventMediator.getPhaseName());
                break;
            case "deleteTask":
                Long id = (Long) Arrays.stream(joinPoint.getArgs()).findFirst()
                        .orElseThrow(() -> new NotFoundException("Id not found"));
                Task task = taskHelper.findTaskById(id);
                if (task.getType().getId() == 1)
                    event.setValue("Удалена фича");
                else if (task.getType().getId() == 2) {
                    event.setValue("Удалена задача");
                }

                event.setPhaseName(eventMediator.getPhaseName());
                event.setTaskName(eventMediator.getTaskName());
                break;
        }
        eventRepository.save(event);
    }
}

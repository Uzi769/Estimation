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
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.EventRepository;
import ru.irlix.evaluation.service.EventService;

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

    @Transactional(readOnly = true)
    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(mapper::eventToEventResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createEvent(JoinPoint joinPoint, Object value) {
        Event event;
        String methodName = joinPoint.getSignature().getName();
        switch (methodName) {
            case "createEstimation":
                EstimationResponse estimationResponse = (EstimationResponse) value;
                event = mapper.estimationResponseToEvent(estimationResponse);
                event.setValue("Создана оценка");
                eventRepository.save(event);
                break;
            case "createPhase":
                PhaseResponse phaseResponse = (PhaseResponse) value;
                event = mapper.phaseResponseToEvent(phaseResponse);
                event.setValue("Создана фаза");
                eventRepository.save(event);
                break;
            case "createTask":
                TaskResponse taskResponse = (TaskResponse) value;
                event = mapper.taskResponseToEvent(taskResponse);
                if (taskResponse.getType() == 1) {
                    event.setValue("Создана фича");
                } else if (taskResponse.getType() == 2) {
                    event.setValue("Создана задача");
                }
                eventRepository.save(event);
                break;
            case "deleteEstimation":
                Estimation estimation = (Estimation) value;
                event = mapper.estimationToEvent(estimation);
                event.setValue("Оценка удалена");
                eventRepository.save(event);
                break;
            case "deletePhase":
                Phase phase = (Phase) value;
                event = mapper.phaseToEvent(phase);
                event.setValue("Фаза удалена");
                eventRepository.save(event);
                break;
            case "deleteTask":
                Task task = (Task) value;
                event = mapper.taskToEvent(task);
                if (task.getType().getId() == 1) {
                    event.setValue("Фича удалена");
                } else if (task.getType().getId() == 2) {
                    event.setValue("Задача удалена");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + methodName);
        }

        eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Object getElementToDelete(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Long id = Arrays.stream(joinPoint.getArgs())
                .mapToLong(a -> (Long) a)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Id not found"));

        switch (methodName) {
            case "deleteEstimation":
                return estimationHelper.findEstimationById(id);
            case "deletePhase":
                return phaseHelper.findPhaseById(id);
            case "deleteTask":
                return taskHelper.findTaskById(id);
            default:
                throw new NotFoundException("Method " + methodName + " not found");
        }
    }
}

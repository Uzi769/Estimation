package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.*;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dao.helper.TaskHelper;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.dao.mapper.EventMapper;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.EventRepository;
import ru.irlix.evaluation.service.EventService;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper mapper;
    private final EstimationMapper estimationMapper;

    private final EstimationHelper estimationHelper;
    private final PhaseHelper phaseHelper;
    private final TaskHelper taskHelper;
    private final UserHelper userHelper;

    @Transactional(readOnly = true)
    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(mapper::eventToEventResponse)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEvent(JoinPoint joinPoint, Object value) {
        Event event;
        String methodName = joinPoint.getSignature().getName();
        switch (methodName) {
            case "createEstimation":
                event = getEvent((EstimationResponse) value);
                break;
            case "createPhase":
                event = getEvent((PhaseResponse) value);
                break;
            case "createTask":
                event = getEvent((TaskResponse) value);
                break;
            case "deleteEstimation":
                event = getEvent((Estimation) value);
                break;
            case "deletePhase":
                event = getEvent((Phase) value);
                break;
            case "deleteTask":
                event = getEvent((Task) value);
                break;
            case "getEstimationsReport":
                event = getReportEvent(joinPoint);
                break;
            case "updateEstimation":
                event = getUserEvent(joinPoint);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + methodName);
        }
        if (event != null)
            eventRepository.save(event);
    }

    private Event getEvent(EstimationResponse estimationResponse) {
        Event event = mapper.estimationResponseToEvent(estimationResponse);
        event.setValue("Создана оценка");
        return event;
    }

    private Event getEvent(PhaseResponse phaseResponse) {
        Event event = mapper.phaseResponseToEvent(phaseResponse);
        event.setValue("Создана фаза");
        return event;
    }

    private Event getEvent(TaskResponse taskResponse) {
        Event event = mapper.taskResponseToEvent(taskResponse);
        if (taskResponse.getType().equals(EntitiesIdConstants.FEATURE_ID)) {
            event.setValue("Создана фича");
        } else if (taskResponse.getType().equals(EntitiesIdConstants.TASK_ID)) {
            event.setValue("Создана задача");
        }
        return event;
    }

    private Event getEvent(Estimation estimation) {
        Event event = mapper.estimationToEvent(estimation);
        event.setValue("Оценка удалена");
        return event;
    }

    private Event getEvent(Phase phase) {
        Event event = mapper.phaseToEvent(phase);
        event.setValue("Фаза удалена");
        return event;
    }

    private Event getEvent(Task task) {
        Event event = mapper.taskToEvent(task);
        if (task.getType().getId().equals(EntitiesIdConstants.FEATURE_ID)) {
            event.setValue("Фича удалена");
        } else if (task.getType().getId().equals(EntitiesIdConstants.TASK_ID)) {
            event.setValue("Задача удалена");
        }
        return event;
    }

    private Event getUserEvent(JoinPoint joinPoint) {
        Event event = null;
        Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
        Long id = (Long) objectEvent;
        Estimation estimation = estimationHelper.findEstimationById(id);
        EstimationRequest estimationRequest = (joinPoint.getArgs()[1] instanceof EstimationRequest) ?
                (EstimationRequest) joinPoint.getArgs()[1]
                : null;
        StringBuilder value = new StringBuilder();
        if (estimationRequest != null) {
            List<User> oldUserList = estimation.getUsers();
            List<User> newUserList = userHelper.findByUserIdIn(estimationRequest.getUserIdList());

            List<User> deletedUserList = oldUserList.stream()
                    .filter(oldUser -> !newUserList.contains(oldUser)).collect(Collectors.toList());
            List<User> insertedUserList = newUserList.stream()
                    .filter(newUser -> !oldUserList.contains(newUser)).collect(Collectors.toList());

            if (insertedUserList.size() != 0) {
                value.append("Добавленные пользователи: ");
                insertedUserList.forEach(user -> setUserString(value, user));
            }
            if (deletedUserList.size() != 0) {
                value.append("Удаленные пользователи: ");
                deletedUserList.forEach(user -> setUserString(value, user));
            }
        }
        if (!value.toString().equals("")) {
            EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
            event = mapper.estimationResponseToEvent(estimationResponse);
            event.setValue(value.toString());
        }
        return event;
    }

    private Event getReportEvent(JoinPoint joinPoint) {
        Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
        Long id = (Long) objectEvent;
        Estimation estimation = estimationHelper.findEstimationById(id);
        EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
        Event event = mapper.estimationResponseToEvent(estimationResponse);
        event.setValue("Отчет выгружен");
        return event;
    }

    private void setUserString(StringBuilder value, User user) {
        value.append(user.getFirstName());
        value.append(" ");
        value.append(user.getLastName());
        value.append(" ");
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

package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
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
    private final TaskHelper taskHelper;
    private final UserHelper userHelper;

    @Transactional(readOnly = true)
    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(mapper::EventToEventResponse)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEvent(JoinPoint joinPoint, Object returnValue) {
        String methodName = joinPoint.getSignature().getName();
        switch (methodName) {
            case "createEstimation":
                EstimationResponse estimationResponse = (EstimationResponse) returnValue;
                Event event = mapper.EstimationResponseToEvent(estimationResponse);
                event.setValue("Создана оценка");
                eventRepository.save(event);
                break;
            case "createPhase":
                PhaseResponse phaseResponse = (PhaseResponse) returnValue;
                event = mapper.phaseResponseToEvent(phaseResponse);
                event.setValue("Создана фаза");
                eventRepository.save(event);
                break;
            case "createTask":
                TaskResponse taskResponse = (TaskResponse) returnValue;
                event = mapper.TaskResponseToEvent(taskResponse);
                if (taskResponse.getType() == 1) {
                    event.setValue("Создана фича");
                } else if (taskResponse.getType() == 2) {
                    event.setValue("Создана задача");
                }
                eventRepository.save(event);
                break;
            case "getEstimationsReport":
                Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
                Long id = (Long) objectEvent;
                Estimation estimation = estimationHelper.findEstimationById(id);
                EstimationResponse estimationResponse1 = estimationMapper.estimationToEstimationResponse(estimation);
                event = mapper.EstimationResponseToEvent(estimationResponse1);
                event.setValue("Отчет выгружен");
                eventRepository.save(event);
                break;
            case "updateEstimation":
                Object objectEvent1 = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
                Long id1 = (Long) objectEvent1;
                Estimation estimation1 = estimationHelper.findEstimationById(id1);
                EstimationRequest estimationRequest = (joinPoint.getArgs()[1] instanceof EstimationRequest) ?
                        (EstimationRequest) joinPoint.getArgs()[1]
                        : null;
                StringBuilder value = new StringBuilder();
                if (estimationRequest != null) {
                    List<User> oldUserList = estimation1.getUsers();
                    List<User> newUserList = userHelper.findByUserIdIn(estimationRequest.getUserIdList());

                    //список удаленных пользователей:
                    List<User> deletedUserList = oldUserList.stream().filter(oldUser ->
                            !newUserList.contains(oldUser)).collect(Collectors.toList());
                    //список добавленных пользователей:
                    List<User> insertedUserList = newUserList.stream().filter(newUser ->
                            !oldUserList.contains(newUser)).collect(Collectors.toList());

                    if (deletedUserList.size() != 0) {
                        value.append("Добавленные пользователи: ");
                        insertedUserList.forEach(user -> setUserString(value, user));
                    }
                    if (insertedUserList.size() != 0) {
                        value.append("Удаленные пользователи: ");
                        deletedUserList.forEach(user -> setUserString(value, user));
                    }
                }
                if (!value.toString().equals("")) {
                    EstimationResponse estimationResponse2 = estimationMapper.estimationToEstimationResponse(estimation1);
                    event = mapper.EstimationResponseToEvent(estimationResponse2);
                    event.setValue(value.toString());
                    eventRepository.save(event);
                }
                break;
        }
    }

    private void setUserString(StringBuilder value, User user) {
        value.append(user.getFirstName());
        value.append(" ");
        value.append(user.getLastName());
        value.append(" ");
    }

    @Transactional
    @Override
    public void deleteEvent(Event eventMediator, String methodName, JoinPoint joinPoint) {
        Event event = new Event();
        event.setUserName(eventMediator.getUserName());
        event.setEstimationName(eventMediator.getEstimationName());
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

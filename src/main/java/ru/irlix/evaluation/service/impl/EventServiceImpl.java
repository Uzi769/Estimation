package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.irlix.evaluation.dao.entity.*;
import ru.irlix.evaluation.dao.helper.*;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.dao.mapper.EventMapper;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dao.mapper.TaskMapper;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.request.EventFilterRequest;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.event.EventRepository;
import ru.irlix.evaluation.service.EventService;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper mapper;
    private final EstimationMapper estimationMapper;
    private final PhaseMapper phaseMapper;
    private final TaskMapper taskMapper;

    private final EstimationHelper estimationHelper;
    private final PhaseHelper phaseHelper;
    private final TaskHelper taskHelper;
    private final UserHelper userHelper;
    private final StatusHelper statusHelper;
    private final FileStorageHelper fileStorageHelper;
    private final ActionHelper actionHelper;

    @Transactional(readOnly = true)
    @Override
    public Page<EventResponse> getAllEvents(EventFilterRequest request) {
        Page<Event> eventsPage = eventRepository.filter(request);
        return mapper.eventToEventResponse(eventsPage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEvent(JoinPoint joinPoint, Object value) {
        Event event = null;
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
                eventRepository.saveAll(getEstimationUpdateEvent(joinPoint));
                break;
            case "updatePhase":
                eventRepository.saveAll(getPhaseUpdateEvent(joinPoint));
                break;
            case "updateTask":
                eventRepository.saveAll(getTaskUpdateEvent(joinPoint));
                break;
            case "storeFileList":
                event = getStoredFileEvent(joinPoint);
                break;
            case "deleteFile":
                event = getEvent((FileStorage) value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + methodName);
        }

        if (event != null) {
            eventRepository.save(event);
        }
    }

    private Event getEvent(EstimationResponse estimationResponse) {
        Event event = mapper.estimationResponseToEvent(estimationResponse);
        event.setDescription("Создана оценка " + event.getEstimationName() + " (" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findCreateAction());
        return event;
    }

    private Event getEvent(PhaseResponse phaseResponse) {
        Event event = mapper.phaseResponseToEvent(phaseResponse);
        event.setDescription("Создана фаза " + event.getPhaseName() + " (" + event.getPhaseId() + ")" + " у оценки "
                + event.getEstimationName() + " (" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findCreateAction());
        return event;
    }

    private Event getEvent(TaskResponse taskResponse) {
        Event event = mapper.taskResponseToEvent(taskResponse);
        String taskTypeAction;
        if (taskResponse.getType().equals(EntitiesIdConstants.FEATURE_ID)) {
            taskTypeAction = "Создана фича ";
        } else {
            taskTypeAction = "Создана задача ";
        }

        event.setDescription(taskTypeAction + event.getTaskName() + " (" + event.getTaskId() + ") у фазы "
                + event.getPhaseName() + " (" + event.getPhaseId() + ") в оценке " + event.getEstimationName()
                + " (" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findCreateAction());
        return event;
    }

    private Event getEvent(Estimation estimation) {
        Event event = mapper.estimationToEvent(estimation);
        event.setDescription("Оценка " + event.getEstimationName() + " (" + event.getEstimationId() + ") удалена");
        event.setAction(actionHelper.findDeleteAction());
        return event;
    }

    private Event getEvent(Phase phase) {
        Event event = mapper.phaseToEvent(phase);
        event.setDescription("Фаза " + event.getPhaseName() + " (" + event.getPhaseId() + ") удалена из оценки "
                + event.getEstimationName() + " (" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findDeleteAction());
        return event;
    }

    private Event getEvent(Task task) {
        Event event = mapper.taskToEvent(task);
        String taskTypeAction;
        if (task.getType().getId().equals(EntitiesIdConstants.FEATURE_ID)) {
            taskTypeAction = "Фича ";
        } else {
            taskTypeAction = "Задача ";
        }

        event.setDescription(taskTypeAction + event.getTaskName() + " (" + event.getTaskId() + ") удалена из фазы "
                + event.getPhaseName() + " (" + event.getPhaseId() + ") в оценке " + event.getEstimationName()
                + " (" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findDeleteAction());
        return event;
    }

    private Event getStoredFileEvent(JoinPoint joinPoint) {
        Event event = null;
        Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
        List<?> multipartFileList = (List<?>) objectEvent;
        List<String> fileNameList = new ArrayList<>();

        Objects.requireNonNull(multipartFileList).forEach(file -> {
            MultipartFile multipartFile = (MultipartFile) file;
            fileNameList.add(multipartFile.getOriginalFilename());
        });

        Estimation estimation = joinPoint.getArgs()[1] instanceof Estimation ?
                (Estimation) joinPoint.getArgs()[1]
                : null;

        if (fileNameList.size() != 0) {
            event = mapper.estimationToEvent(estimation);
            String value = "Файл(ы): " + fileNameList + " прикреплен(ы) к оценке "
                    + Objects.requireNonNull(estimation).getName()
                    + "(" + estimation.getId() + ")";
            event.setDescription(value);
            event.setAction(actionHelper.findEditFilesAction());
        }

        return event;
    }

    private Event getEvent(FileStorage fileStorage) {
        Event event = mapper.estimationToEvent(fileStorage.getEstimation());
        event.setDescription("Файл " + fileStorage.getFileName() + " удален из оценки "
                + event.getEstimationName() + "(" + event.getEstimationId() + ")");
        event.setAction(actionHelper.findEditFilesAction());
        return event;
    }

    private List<Event> getEstimationUpdateEvent(JoinPoint joinPoint) {
        List<Event> events = new ArrayList<>();
        Object objectId = Arrays.stream(joinPoint.getArgs())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Id not found in arguments"));

        Long id = (Long) objectId;
        Estimation estimation = estimationHelper.findEstimationById(id);

        if (joinPoint.getArgs()[1] == null) {
            return events;
        }

        EstimationRequest estimationRequest = (EstimationRequest) joinPoint.getArgs()[1];

        Long newStatusId = estimationRequest.getStatus();
        if (newStatusId != null) {
            Long oldStatusId = estimation.getStatus().getId();
            if (!Objects.equals(oldStatusId, newStatusId)) {
                EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
                Event event = mapper.estimationResponseToEvent(estimationResponse);
                String newStatus = statusHelper.findStatusById(newStatusId).getDisplayValue();
                event.setDescription("У оценки " + estimation.getName() + " (" + estimation.getId() + ") сменен статус с \""
                        + estimation.getStatus().getDisplayValue() + "\" на \"" + newStatus + "\"");
                event.setAction(actionHelper.findChangeStatusAction());
                events.add(event);
            }
        }

        String newName = estimationRequest.getName();
        if (newName != null) {
            String oldName = estimation.getName();
            if (!Objects.equals(newName, oldName)) {
                EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
                Event event = mapper.estimationResponseToEvent(estimationResponse);
                event.setDescription("Оценка " + estimation.getName() + " (" + estimation.getId() + ") переименована на "
                        + newName);
                event.setAction(actionHelper.findRenameAction());
                events.add(event);
            }
        }

        if (estimationRequest.getUserIdList() != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("Изменение участиников оценки ").append(estimation.getName()).append(" (")
                    .append(estimation.getId()).append(") ");
            List<User> oldUserList = estimation.getUsers();
            List<User> newUserList = userHelper.findByUserIdIn(estimationRequest.getUserIdList());

            List<User> deletedUserList = oldUserList.stream()
                    .filter(oldUser -> !newUserList.contains(oldUser)).collect(Collectors.toList());
            List<User> insertedUserList = newUserList.stream()
                    .filter(newUser -> !oldUserList.contains(newUser)).collect(Collectors.toList());

            if (insertedUserList.size() != 0) {
                builder.append("Добавленные пользователи: ");
                List<String> userList = insertedUserList.stream()
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .collect(Collectors.toList());
                builder.append(userList).append(" ");
            }

            if (deletedUserList.size() != 0) {
                builder.append("Удаленные пользователи: ");
                List<String> userList = deletedUserList.stream()
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .collect(Collectors.toList());
                builder.append(userList);
            }

            String log = builder.toString();
            if (log.chars().count() > 250) {
                log = "Изменение участников оценки  (" + estimation.getId()
                        + "), значительные изменения состава экспертов";
            }

            if (!log.equals("")) {
                EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
                Event event = mapper.estimationResponseToEvent(estimationResponse);
                event.setDescription(log);
                event.setAction(actionHelper.findEditUsersAction());
                events.add(event);
            }
        }

        return events;
    }

    private List<Event> getPhaseUpdateEvent(JoinPoint joinPoint) {
        List<Event> events = new ArrayList<>();
        Object objectId = Arrays.stream(joinPoint.getArgs())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Id not found in arguments"));

        Long id = (Long) objectId;
        Phase phase = phaseHelper.findPhaseById(id);

        if (joinPoint.getArgs()[1] == null) {
            return events;
        }

        PhaseRequest phaseRequest = (PhaseRequest) joinPoint.getArgs()[1];

        Boolean newDone = phaseRequest.getDone();
        if (newDone != null) {
            Boolean oldDone = phase.getDone();
            if (!Objects.equals(oldDone, newDone)) {
                PhaseResponse phaseResponse = phaseMapper.phaseToPhaseResponse(phase);
                Event event = mapper.phaseResponseToEvent(phaseResponse);
                if (newDone) {
                    event.setDescription("Фаза " + phase.getName() + " (" + phase.getId() + ") переведена в статус \"Завершена\"");
                } else {
                    event.setDescription("Фаза " + phase.getName() + " (" + phase.getId() + ") переведена в статус \"В работе\"");
                }

                event.setAction(actionHelper.findChangeStatusAction());
                events.add(event);
            }
        }

        String newName = phaseRequest.getName();
        if (newName != null) {
            String oldName = phase.getName();
            if (!Objects.equals(newName, oldName)) {
                PhaseResponse phaseResponse = phaseMapper.phaseToPhaseResponse(phase);
                Event event = mapper.phaseResponseToEvent(phaseResponse);
                event.setDescription("Фаза " + phase.getName() + " (" + phase.getId() + ") переименована на " + newName);
                event.setAction(actionHelper.findRenameAction());
                events.add(event);
            }
        }

        return events;
    }

    private List<Event> getTaskUpdateEvent(JoinPoint joinPoint) {
        List<Event> events = new ArrayList<>();
        Object objectId = Arrays.stream(joinPoint.getArgs())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Id not found in arguments"));

        Long id = (Long) objectId;
        Task task = taskHelper.findTaskById(id);

        if (joinPoint.getArgs()[1] == null) {
            return events;
        }

        TaskRequest taskRequest = (TaskRequest) joinPoint.getArgs()[1];

        String newName = taskRequest.getName();
        if (newName != null) {
            String oldName = task.getName();
            if (!Objects.equals(newName, oldName)) {
                TaskResponse taskResponse = taskMapper.taskToResponse(task);
                Event event = mapper.taskResponseToEvent(taskResponse);
                if (task.getType().getId().equals(EntitiesIdConstants.FEATURE_ID)) {
                    event.setDescription("Фича " + task.getName() + " (" + task.getId() + ") переименована на "
                            + newName);
                } else if (task.getType().getId().equals(EntitiesIdConstants.TASK_ID)) {
                    event.setDescription("Задача " + task.getName() + " (" + task.getId() + ") переименована на "
                            + newName);
                }
                event.setAction(actionHelper.findRenameAction());
                events.add(event);
            }
        }

        return events;
    }

    private Event getReportEvent(JoinPoint joinPoint) {
        Object objectEvent = Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);
        Long id = (Long) objectEvent;
        Estimation estimation = estimationHelper.findEstimationById(id);
        EstimationResponse estimationResponse = estimationMapper.estimationToEstimationResponse(estimation);
        Event event = mapper.estimationResponseToEvent(estimationResponse);
        event.setDescription("Отчет по оценке " + estimation.getName() + " (" + estimation.getId() + ") выгружен");
        event.setAction(actionHelper.findLoadReportAction());
        return event;
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
            case "deleteFile":
                return fileStorageHelper.findFileById(id);
            default:
                throw new NotFoundException("Method " + methodName + " not found");
        }
    }
}

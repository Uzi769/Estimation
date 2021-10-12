package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.*;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.TaskMapper;
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dao.helper.RoleHelper;
import ru.irlix.evaluation.dao.helper.TaskTypeHelper;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.request.TaskUpdateRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.TaskRepository;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final TaskTypeHelper taskTypeHelper;
    private final UserHelper userHelper;
    private final RoleHelper roleHelper;
    private final PhaseHelper phaseHelper;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = mapper.taskRequestToTask(request);
        checkAccessToEstimation(task);
        Task savedTask = taskRepository.save(task);

        log.info("Task with id " + savedTask.getId() + " saved");
        return mapper.taskToResponse(savedTask);
    }

    @Override
    @Transactional
    public List<TaskResponse> createTasks(List<TaskRequest> requests) {
        List<Task> tasks = mapper.taskRequestToTask(requests);
        List<Task> taskWithAccess = getTasksWithAccess(tasks);
        List<Task> savedTasks = taskRepository.saveAll(taskWithAccess);

        log.info("Task list saved");
        return mapper.taskToResponse(savedTasks);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = updateTaskById(id, taskRequest);
        Task savedTask = taskRepository.save(task);

        log.info("Task with id " + savedTask.getId() + " updated");
        return mapper.taskToResponse(savedTask);
    }

    @Override
    @Transactional
    public List<TaskResponse> updateTasks(List<TaskUpdateRequest> tasksRequest) {
        List<Task> updatedTasks = tasksRequest.stream()
                .map(request -> updateTaskById(request.getId(), request))
                .collect(Collectors.toList());

        List<Task> tasksWithAccess = getTasksWithAccess(updatedTasks);
        List<Task> savedTasks = taskRepository.saveAll(tasksWithAccess);

        log.info("Task list updated");
        return mapper.taskToResponse(savedTasks);
    }

    private Task updateTaskById(Long id, TaskRequest request) {
        Task taskToUpdate = findTaskById(id);
        checkAndUpdateFields(taskToUpdate, request);
        return taskToUpdate;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse findTaskResponseById(Long id) {
        Task task = findTaskById(id);
        log.info("Task with id " + task.getId() + " found");
        return mapper.taskToResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> findTasks(Long phaseId) {
        List<Task> tasks = taskRepository.findByPhaseId(phaseId);
        log.info("Tasks on phase with id " + phaseId + " found");
        return mapper.taskToResponse(tasks);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
        log.info("Task with id " + task.getId() + " deleted");
    }

    private Task findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"));

        checkAccessToEstimation(task);
        return task;
    }

    private void checkAccessToEstimation(Task task) {
        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        if (!SecurityUtils.hasAccessToAllEstimations() && !task.getPhase().getEstimation().getUsers().contains(user)) {
            throw new AccessDeniedException("User with id " + keycloakId + " cant get access to estimation");
        }
    }

    private List<Task> getTasksWithAccess(List<Task> tasks) {
        if (SecurityUtils.hasAccessToAllEstimations()) {
            return tasks;
        }

        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        return tasks.stream()
                .filter(t -> !t.getPhase().getEstimation().getUsers().contains(user))
                .collect(Collectors.toList());
    }

    private void checkAndUpdateFields(Task task, TaskRequest request) {
        if (request.getName() != null) {
            task.setName(request.getName());
        }

        if (request.getType() != null) {
            TaskTypeDictionary type = taskTypeHelper.findTypeById(request.getType());
            task.setType(type);
        }

        if (request.getPmReserve() != null) {
            task.setPmReserve(request.getPmReserve());
        }

        if (request.getQaReserve() != null) {
            task.setQaReserve(request.getQaReserve());
        }

        if (request.getBugsReserve() != null) {
            task.setBugsReserve(request.getBugsReserve());
        }

        if (request.getPmReserveOn() != null) {
            task.setPmReserveOn(request.getPmReserveOn());
        }

        if (request.getQaReserveOn() != null) {
            task.setQaReserveOn(request.getQaReserveOn());
        }

        if (request.getBugsReserveOn() != null) {
            task.setBugsReserveOn(request.getBugsReserveOn());
        }

        if (request.getComment() != null) {
            task.setComment(request.getComment());
        }

        if (request.getPhaseId() != null) {
            Phase phase = phaseHelper.findPhaseById(request.getPhaseId());
            task.setPhase(phase);
        }

        if (EntitiesIdConstants.TASK_ID.equals(task.getType().getId())) {
            if (request.getRepeatCount() != null) {
                task.setRepeatCount(request.getRepeatCount());
            }

            if (request.getMinHours() != null) {
                task.setMinHours(request.getMinHours());
            }

            if (request.getMaxHours() != null) {
                task.setMaxHours(request.getMaxHours());
            }

            if (request.getRoleId() != null) {
                Role role = roleHelper.findRoleById(request.getRoleId());
                task.setRole(role);
            }

            if (request.getFeatureId() != null) {
                Task parent = findTaskById(request.getFeatureId());
                task.setFeature(parent);
            }
        }
    }
}

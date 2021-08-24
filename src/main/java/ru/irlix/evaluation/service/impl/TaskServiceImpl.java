package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.dao.mapper.TaskMapper;
import ru.irlix.evaluation.dao.mapper.helper.PhaseHelper;
import ru.irlix.evaluation.dao.mapper.helper.RoleHelper;
import ru.irlix.evaluation.dao.mapper.helper.TaskTypeHelper;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.repository.TaskRepository;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.EntityConstants;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final TaskTypeHelper taskTypeHelper;
    private final RoleHelper roleHelper;
    private final PhaseHelper phaseHelper;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = mapper.taskRequestToTask(request);
        Task savedTask = taskRepository.save(task);

        return mapper.taskToResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = findTaskById(id);
        checkAndUpdateFields(task, taskRequest);
        Task savedTask = taskRepository.save(task);

        return mapper.taskToResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse findTaskResponseById(Long id) {
        Task task = findTaskById(id);
        return mapper.taskToResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TaskResponse> findTasks(Long phaseId) {
        Phase phase = phaseHelper.findPhaseById(phaseId);
        Set<Task> tasks = phase.getTasks();

        return mapper.taskToResponse(tasks);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"));
    }

    private void checkAndUpdateFields(Task task, TaskRequest request) {
        if (request.getName() != null) {
            task.setName(request.getName());
        }

        if (request.getType() != null) {
            TaskTypeDictionary type = taskTypeHelper.findTypeById(request.getType());
            task.setType(type);
        }

        if (request.getManagementReserve() != null) {
            task.setManagementReserve(request.getManagementReserve());
        }

        if (request.getQaReserve() != null) {
            task.setQaReserve(request.getQaReserve());
        }

        if (request.getBagsReserve() != null) {
            task.setBagsReserve(request.getBagsReserve());
        }

        if (request.getManagementReserveOn() != null) {
            task.setManagementReserveOn(request.getManagementReserveOn());
        }

        if (request.getQaReserveOn() != null) {
            task.setQaReserveOn(request.getQaReserveOn());
        }

        if (request.getBagsReserveOn() != null) {
            task.setBagsReserveOn(request.getBagsReserveOn());
        }

        if (request.getComment() != null) {
            task.setComment(request.getComment());
        }

        if (request.getPhaseId() != null) {
            Phase phase = phaseHelper.findPhaseById(request.getPhaseId());
            task.setPhase(phase);
        }

        if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
            if (request.getRepeatCount() != null) {
                task.setRepeatCount(request.getRepeatCount());
            }

            if (request.getHoursMin() != null) {
                task.setHoursMin(request.getHoursMin());
            }

            if (request.getHoursMax() != null) {
                task.setHoursMax(request.getHoursMax());
            }

            if (request.getRoleId() != null) {
                Role role = roleHelper.findRoleById(request.getRoleId());
                task.setRole(role);
            }

            if (request.getFeatureId() != null) {
                Task parent = findTaskById(request.getFeatureId());
                task.setParent(parent);
            }
        }
    }
}

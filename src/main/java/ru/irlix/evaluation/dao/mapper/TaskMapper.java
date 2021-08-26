package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.dao.mapper.helper.PhaseHelper;
import ru.irlix.evaluation.dao.mapper.helper.RoleHelper;
import ru.irlix.evaluation.dao.mapper.helper.TaskHelper;
import ru.irlix.evaluation.dao.mapper.helper.TaskTypeHelper;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.utils.EntityConstants;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Autowired
    protected PhaseHelper phaseHelper;

    @Autowired
    protected TaskHelper taskHelper;

    @Autowired
    protected TaskTypeHelper taskTypeHelper;

    @Autowired
    protected RoleHelper roleHelper;

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "hoursMax", ignore = true)
    @Mapping(target = "hoursMin", ignore = true)
    public abstract Task taskRequestToTask(TaskRequest taskRequest);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    public abstract TaskResponse taskToResponse(Task task);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    public abstract List<TaskResponse> taskToResponse(List<Task> tasks);

    @AfterMapping
    protected void map(@MappingTarget Task task, TaskRequest request) {
        Phase phase = phaseHelper.findPhaseById(request.getPhaseId());
        task.setPhase(phase);

        TaskTypeDictionary type = taskTypeHelper.findTypeById(request.getType());
        task.setType(type);

        if (request.getHoursMax() != null) {
            task.setHoursMax(request.getHoursMax());
        }

        if (request.getHoursMin() != null) {
            task.setHoursMin(request.getHoursMin());
        }

        if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
            if (request.getRoleId() != null) {
                Role role = roleHelper.findRoleById(request.getRoleId());
                task.setRole(role);
            }

            if (request.getFeatureId() != null) {
                Task feature = taskHelper.findTaskById(request.getFeatureId());
                task.setParent(feature);
            }
        }
    }

    @AfterMapping
    protected void map(@MappingTarget TaskResponse response, Task task) {
        response.setPhaseId(task.getPhase().getId());

        if (task.getType() != null) {
            response.setType(task.getType().getId());
        }

        if (task.getParent() != null) {
            response.setParentId(task.getParent().getId());
        }

        if (task.getRole() != null) {
            response.setRoleId(task.getRole().getId());
        }

        if (task.getTasks() != null) {
            response.setTasks(taskToResponse(task.getTasks()));
        }
    }
}

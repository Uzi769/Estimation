package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    protected PhaseHelper phaseHelper;
    protected TaskHelper taskHelper;
    protected TaskTypeHelper taskTypeHelper;
    protected RoleHelper roleHelper;

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "parent", ignore = true)
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
    public abstract Set<TaskResponse> taskToResponse(Set<Task> tasks);

    @AfterMapping
    protected void map(@MappingTarget Task task, TaskRequest request) {
        Phase phase = phaseHelper.findPhaseById(request.getPhaseId());
        task.setPhase(phase);

        TaskTypeDictionary type = taskTypeHelper.findTypeById(request.getType());
        task.setType(type);

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
            response.setType(task.getType().getValue());
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

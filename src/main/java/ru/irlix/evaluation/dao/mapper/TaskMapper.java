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
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dao.helper.RoleHelper;
import ru.irlix.evaluation.dao.helper.TaskHelper;
import ru.irlix.evaluation.dao.helper.TaskTypeHelper;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.math.EstimationMath;

import java.util.List;

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

    @Autowired
    protected EstimationMath math;

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "feature", ignore = true)
    public abstract Task taskRequestToTask(TaskRequest taskRequest);

    public abstract List<Task> taskRequestToTask(List<TaskRequest> taskRequest);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "featureId", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    public abstract TaskResponse taskToResponse(Task task);

    public abstract List<TaskResponse> taskToResponse(List<Task> tasks);

    @AfterMapping
    protected void map(@MappingTarget Task task, TaskRequest request) {
        Phase phase = phaseHelper.findPhaseById(request.getPhaseId());
        task.setPhase(phase);

        if (request.getType() == null) {
            request.setType(EntitiesIdConstants.TASK_ID);
        }

        TaskTypeDictionary type = taskTypeHelper.findTypeById(request.getType());
        task.setType(type);

        if (EntitiesIdConstants.TASK_ID.equals(task.getType().getId())) {
            if (request.getRoleId() == null) {
                request.setRoleId(EntitiesIdConstants.DEFAULT_ROLE_ID);
            }

            Role role = roleHelper.findRoleById(request.getRoleId());
            task.setRole(role);

            if (request.getFeatureId() != null) {
                Task feature = taskHelper.findTaskById(request.getFeatureId());
                task.setFeature(feature);
            }
        }
    }

    @AfterMapping
    protected void map(@MappingTarget TaskResponse response, Task task) {
        response.setPhaseId(task.getPhase().getId());

        if (task.getType() != null) {
            response.setType(task.getType().getId());
        }

        if (task.getFeature() != null) {
            response.setFeatureId(task.getFeature().getId());
        }

        if (task.getRole() != null) {
            response.setRoleId(task.getRole().getId());
        }

        if (EntitiesIdConstants.FEATURE_ID.equals(task.getType().getId()) && task.getTasks() != null) {
            response.setTasks(taskToResponse(task.getTasks()));

            int tasksRepeatCountSum = 0;
            double tasksHoursMinSum = 0;
            double tasksHoursMaxSum = 0;

            for (Task nestedTask : task.getTasks()) {
                tasksRepeatCountSum += nestedTask.getRepeatCount();
                tasksHoursMinSum += nestedTask.getMinHours();
                tasksHoursMaxSum += nestedTask.getMaxHours();
            }

            response.setRepeatCount(tasksRepeatCountSum);
            response.setMinHours(math.roundToHalf(tasksHoursMinSum));
            response.setMaxHours(math.roundToHalf(tasksHoursMaxSum));
        }
    }
}

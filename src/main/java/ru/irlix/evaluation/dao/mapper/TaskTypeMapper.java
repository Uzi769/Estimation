package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.dto.request.TaskTypeRequest;
import ru.irlix.evaluation.dto.response.TaskTypeResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskTypeMapper {

    TaskTypeDictionary taskTypeRequestToTaskType(TaskTypeRequest taskTypeRequest);

    TaskTypeResponse taskTypeToTaskTypeResponse(TaskTypeDictionary taskType);

    List<TaskTypeResponse> taskTypeToTaskTypeResponse(List<TaskTypeDictionary> taskTypes);
}

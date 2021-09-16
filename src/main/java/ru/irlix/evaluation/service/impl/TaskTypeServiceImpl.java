package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.dao.mapper.TaskTypeMapper;
import ru.irlix.evaluation.dto.request.TaskTypeRequest;
import ru.irlix.evaluation.dto.response.TaskTypeResponse;
import ru.irlix.evaluation.repository.TaskTypeRepository;
import ru.irlix.evaluation.service.TaskTypeService;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private final TaskTypeRepository taskTypeRepository;
    private final TaskTypeMapper mapper;

    @Override
    @Transactional
    public TaskTypeResponse createTaskType(TaskTypeRequest taskTypeRequest) {
        TaskTypeDictionary taskType = mapper.taskTypeRequestToTaskType(taskTypeRequest);
        TaskTypeDictionary savedTaskType = taskTypeRepository.save(taskType);

        log.info("Task with id " + savedTaskType.getId() + " saved");
        return mapper.taskTypeToTaskTypeResponse(savedTaskType);
    }

    @Override
    @Transactional
    public TaskTypeResponse updateTaskType(Long id, TaskTypeRequest taskTypeRequest) {
        TaskTypeDictionary taskType = findTaskTypeById(id);
        checkAndUpdateFields(taskType, taskTypeRequest);
        TaskTypeDictionary savedTaskType = taskTypeRepository.save(taskType);

        log.info("Task with id " + savedTaskType.getId() + " updated");
        return mapper.taskTypeToTaskTypeResponse(savedTaskType);
    }

    @Override
    @Transactional
    public void deleteTaskType(Long id) {
        TaskTypeDictionary taskType = findTaskTypeById(id);
        taskTypeRepository.delete(taskType);
        log.info("Task with id " + taskType.getId() + " deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public TaskTypeResponse findTaskTypeResponseById(Long id) {
        TaskTypeDictionary taskType = findTaskTypeById(id);
        log.info("Task with id " + taskType.getId() + " found");
        return mapper.taskTypeToTaskTypeResponse(taskType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskTypeResponse> findAllTaskTypes() {
        List<TaskTypeDictionary> taskTypes = taskTypeRepository.findAll();
        log.info("All tasks type found");
        return mapper.taskTypeToTaskTypeResponse(taskTypes);
    }

    private TaskTypeDictionary findTaskTypeById(Long id) {
        return taskTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task type with id " + id + " not found"));
    }

    private void checkAndUpdateFields(TaskTypeDictionary taskType, TaskTypeRequest taskTypeRequest) {
        if (taskTypeRequest.getValue() != null) {
            taskType.setValue(taskTypeRequest.getValue());
        }
    }
}

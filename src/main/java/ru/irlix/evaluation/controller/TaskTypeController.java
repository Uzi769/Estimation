package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.TaskTypeRequest;
import ru.irlix.evaluation.dto.response.TaskTypeResponse;
import ru.irlix.evaluation.service.TaskTypeService;
import ru.irlix.evaluation.utils.UrlConstants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/types")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class TaskTypeController {

    private final TaskTypeService taskTypeService;

    @PostMapping
    public TaskTypeResponse createTaskType(@Valid TaskTypeRequest request) {
        return taskTypeService.createTaskType(request);
    }

    @PutMapping("/{id}")
    public TaskTypeResponse updateTaskType(@PathVariable @Positive(message = "{id.positive}") Long id,
                                           @Valid TaskTypeRequest request) {
        return taskTypeService.updateTaskType(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskType(@PathVariable @Positive(message = "{id.positive}") Long id) {
        taskTypeService.deleteTaskType(id);
    }

    @GetMapping
    public List<TaskTypeResponse> findAllTaskTypes() {
        return taskTypeService.findAllTaskTypes();
    }

    @GetMapping("/{id}")
    public TaskTypeResponse findTaskTypeById(@PathVariable @Positive(message = "{id.positive}") Long id) {
        return taskTypeService.findTaskTypeResponseById(id);
    }
}

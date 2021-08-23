package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.UrlConstants;

import java.util.Set;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @GetMapping("/{id}")
    public TaskResponse findTaskById(@PathVariable Long id) {
        return taskService.findTaskResponseById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping()
    public Set<TaskResponse> findTasksByPhaseId(@RequestParam("phaseId") Long id) {
        return taskService.findTasksByPhase(id);
    }
}

package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Log4j2
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

        log.info("Task with id " + savedTask.getId() + " saved");
        return mapper.taskToResponse(savedTask);
    }

    @Override
    @Transactional
    public List<TaskResponse> createTasks(List<TaskRequest> requests) {
        List<Task> tasks = mapper.taskRequestToTask(requests);
        List<Task> savedTasks = taskRepository.saveAll(tasks);

        return mapper.taskToResponse(savedTasks);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = findTaskById(id);
        checkAndUpdateFields(task, taskRequest);
        Task savedTask = taskRepository.save(task);

        log.info("Task with id " + savedTask.getId() + " updated");
        return mapper.taskToResponse(savedTask);
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

    @Override
    public void unloadingTasks() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Tasks sheet");

        List<Task> tasks = taskRepository.findAll();
        int rowNum = 0;

        Row row = sheet.createRow(rowNum);

        //Header
        setCell(row, "Id", 0);
        setCell(row, "Name", 1);
        setCell(row, "Type", 2);
        setCell(row, "RepeatCount", 3);
        setCell(row, "BagsReserve", 4);

        setCell(row, "QaReserve", 5);
        setCell(row, "ManagementReserve", 6);
        setCell(row, "Comment", 7);
        setCell(row, "HoursMin", 8);
        setCell(row, "HoursMax", 9);
        setCell(row, "Phase", 10);
        setCell(row, "EstimationRole", 11);
        setCell(row, "ParentId", 12);
        setCell(row, "BagsReserveOn", 13);
        setCell(row, "ManagementReserveOn", 14);
        setCell(row, "QaReserveOn", 15);

        //Data
        for (Task task : tasks) {
            rowNum++;
            row = sheet.createRow(rowNum);
            setCell(row, task.getId().toString(), 0);
            setCell(row, task.getName(), 1);
            setCell(row, task.getType().getValue(), 2);
            setCell(row, task.getRepeatCount().toString(), 3);
            setCell(row, task.getBagsReserve().toString(), 4);
            setCell(row, task.getQaReserve().toString(), 5);
            setCell(row, task.getManagementReserve().toString(), 6);
            setCell(row, task.getComment(), 7);
            setCell(row, task.getHoursMin().toString(), 8);
            setCell(row, task.getHoursMax().toString(), 9);
            setCell(row, task.getPhase().getId().toString(), 10);
            setCell(row, task.getRole().getDisplayValue(), 11);
            setCell(row, task.getParent().getId().toString(), 12);
            setCell(row, task.getBagsReserveOn().toString(), 13);
            setCell(row, task.getManagementReserveOn().toString(), 14);
            setCell(row, task.getQaReserveOn().toString(), 15);
        }

        File file = new File("C:/output/tasks.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        log.info("unloading tasks into " + file.getAbsolutePath());
    }

    private void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
    }
}

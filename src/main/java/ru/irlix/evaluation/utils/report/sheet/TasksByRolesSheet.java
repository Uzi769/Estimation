package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.util.List;
import java.util.stream.Collectors;

public class TasksByRolesSheet extends Sheet {

    public TasksByRolesSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Task by roles");
        configureColumns();

        fillHeader("Фичи");

        for (Phase phase : estimation.getPhases()) {
            List<Task> features = phase.getTasks().stream()
                    .filter(this::isFeature)
                    .collect(Collectors.toList());

            if (features.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, features);

            for (Task task : features) {
                fillFeatureRow(task, request);
            }
        }

        createRow(super.ROW_HEIGHT);
        fillHeader("Задачи");

        for (Phase phase : estimation.getPhases()) {
            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> !isFeature(t))
                    .collect(Collectors.toList());

            if (tasks.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, tasks);

            for (Task task : tasks) {
                fillTaskRow(task, request);
            }
        }

        createRow(ROW_HEIGHT);
        fillSummary();
    }

    private void fillHeader(String taskType) {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setHeaderCell(row, taskType, 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 4);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request, List<Task> tasks) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);

        double sumHoursMin = ReportMath.calcListSummaryMinHoursWithoutQaAndPm(tasks, request);
        hoursMinSummary += sumHoursMin;
        helper.setMarkedCell(row, sumHoursMin, 3);

        double sumCostMin = ReportMath.calcListSummaryMinCostWithoutQaAndPm(tasks, request);
        costMinSummary += sumCostMin;
        helper.setMarkedCell(row, sumCostMin, 4);

        double sumHoursMax = ReportMath.calcListSummaryMaxHoursWithoutQaAndPm(tasks, request);
        hoursMaxSummary += sumHoursMax;
        helper.setMarkedCell(row, sumHoursMax, 5);

        double sumCostMax = ReportMath.calcListSummaryMaxCostWithoutQaAndPm(tasks, request);
        costMaxSummary += sumCostMax;
        helper.setMarkedCell(row, sumCostMax, 6);

        helper.setMarkedCell(row, null, 7);
    }

    private void fillFeatureRow(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setCell(row, feature.getComment(), 7);
    }

    private void fillTaskRow(Task task, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setCell(row, task.getName(), 1);
        helper.setCell(row, ReportMath.calcTaskMinHours(task, request), 3);
        helper.setCell(row, ReportMath.calcTaskMinCost(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMaxHours(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxCost(task, request), 6);
        helper.setCell(row, task.getComment(), 7);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursMinSummary, 3);
        helper.setMarkedCell(row, costMinSummary, 4);
        helper.setMarkedCell(row, hoursMaxSummary, 5);
        helper.setMarkedCell(row, costMaxSummary, 6);
        helper.setMarkedCell(row, null, 7);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 12000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 12000);
    }
}

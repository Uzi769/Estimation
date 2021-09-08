package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.enums.TableType;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TasksByRolesSheet extends EstimationReportSheet {

    private double otherTasksMinHoursSummary;
    private double otherTasksMinCostSummary;
    private double otherTasksMaxHoursSummary;
    private double otherTasksMaxCostSummary;

    public TasksByRolesSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка по фичам");
        configureColumns();

        fillReportHeader(estimation, request, 7);

        fillTableHeader("Фичи", false);

        for (Phase phase : estimation.getPhases()) {
            List<Task> features = phase.getTasks().stream()
                    .filter(this::isFeature)
                    .collect(Collectors.toList());

            if (features.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, features, false);

            for (Task task : features) {
                fillFeatureRow(task, request);
            }
        }

        fillSummary(TableType.DEFAULT_TABLE);

        createRow(super.ROW_HEIGHT);
        fillTableHeader("Задачи", true);

        for (Phase phase : estimation.getPhases()) {
            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> !isFeature(t))
                    .collect(Collectors.toList());

            if (tasks.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, tasks, true);

            for (Task task : tasks) {
                fillTaskRow(task, request);
            }
        }

        fillSummary(TableType.LIGHT_TABLE);

        createRow(ROW_HEIGHT);
        fillSummary(TableType.NONE);
    }

    private void fillTableHeader(String taskType, boolean isLight) {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        if (isLight) {
            helper.setLightHeaderCell(row, taskType, 0);
            helper.setLightHeaderCell(row, "Часы (мин)", 3);
            helper.setLightHeaderCell(row, "Стоимость (мин), RUB", 4);
            helper.setLightHeaderCell(row, "Часы (наиболее вероятные)", 5);
            helper.setLightHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
            helper.setLightHeaderCell(row, "Комментарии", 7);

            helper.setLightHeaderCell(row, null, 1);
            helper.setLightHeaderCell(row, null, 2);
        } else {
            helper.setHeaderCell(row, taskType, 0);
            helper.setHeaderCell(row, "Часы (мин)", 3);
            helper.setHeaderCell(row, "Стоимость (мин), RUB", 4);
            helper.setHeaderCell(row, "Часы (наиболее вероятные)", 5);
            helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
            helper.setHeaderCell(row, "Комментарии", 7);

            helper.setHeaderCell(row, null, 1);
            helper.setHeaderCell(row, null, 2);
        }
    }

    private void fillPhaseRow(Phase phase, ReportRequest request, List<Task> tasks, boolean isLight) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        double sumHoursMin = ReportMath.calcListSummaryMinHours(tasks, request);
        double sumCostMin = ReportMath.calcListSummaryMinCost(tasks, request);
        double sumHoursMax = ReportMath.calcListSummaryMaxHours(tasks, request);
        double sumCostMax = ReportMath.calcListSummaryMaxCost(tasks, request);

        if (isLight) {
            helper.setLightCell(row, phase.getName(), 0);
            helper.setLightCell(row, sumHoursMin, 3);
            helper.setLightCell(row, sumCostMin, 4);
            helper.setLightCell(row, sumHoursMax, 5);
            helper.setLightCell(row, sumCostMax, 6);

            helper.setLightCell(row, null, 1);
            helper.setLightCell(row, null, 2);
            helper.setLightCell(row, null, 7);

            otherTasksMinHoursSummary += sumHoursMin;
            otherTasksMinCostSummary += sumCostMin;
            otherTasksMaxHoursSummary += sumHoursMax;
            otherTasksMaxCostSummary += sumCostMax;
        } else {
            helper.setMarkedCell(row, phase.getName(), 0);
            helper.setMarkedCell(row, sumHoursMin, 3);
            helper.setMarkedCell(row, sumCostMin, 4);
            helper.setMarkedCell(row, sumHoursMax, 5);
            helper.setMarkedCell(row, sumCostMax, 6);

            helper.setMarkedCell(row, null, 1);
            helper.setMarkedCell(row, null, 2);
            helper.setMarkedCell(row, null, 7);

            hoursMinSummary += sumHoursMin;
            costMinSummary += sumCostMin;
            hoursMaxSummary += sumHoursMax;
            costMaxSummary += sumCostMax;
        }
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

        helper.setCell(row, null, 0);

        fillRoleRow(feature.getTasks(), request);
    }

    private void fillTaskRow(Task task, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setCell(row, task.getName(), 1);
        helper.setCell(row, ReportMath.calcTaskMinHoursWithQaAndPm(task, request), 3);
        helper.setCell(row, ReportMath.calcTaskMinCostWithQaAndPm(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMaxHoursWithQaAndPm(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxCostWithQaAndPm(task, request), 6);
        helper.setCell(row, task.getComment(), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 2);

        fillRoleRow(task, request);
    }

    private void fillRoleRow(Task task, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, task.getRole().getDisplayValue(), 2);
        helper.setCell(row, ReportMath.calcTaskMinHours(task, request), 3);
        helper.setCell(row, ReportMath.calcTaskMinCost(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMaxHours(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxCost(task, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);

        if (ReportMath.calcQaMaxHours(task, request) > 0) {
            row = createRow(ROW_HEIGHT);

            helper.setCell(row, "Специалист по тестированию", 2);
            helper.setCell(row, ReportMath.calcQaMinHours(task, request), 3);
            helper.setCell(row, ReportMath.calcQaMinCost(task, request), 4);
            helper.setCell(row, ReportMath.calcQaMaxHours(task, request), 5);
            helper.setCell(row, ReportMath.calcQaMaxCost(task, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (ReportMath.calcPmMaxCost(task, request) > 0) {
            row = createRow(ROW_HEIGHT);

            helper.setCell(row, "Руководитель", 2);
            helper.setCell(row, ReportMath.calcPmMinHours(task, request), 3);
            helper.setCell(row, ReportMath.calcPmMinCost(task, request), 4);
            helper.setCell(row, ReportMath.calcPmMaxHours(task, request), 5);
            helper.setCell(row, ReportMath.calcPmMaxCost(task, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }
    }

    private void fillRoleRow(List<Task> tasks, ReportRequest request) {
        Map<Role, List<Task>> tasksByRole = tasks.stream().collect(Collectors.groupingBy(Task::getRole));

        for (Role role : tasksByRole.keySet()) {
            Row row = createRow(ROW_HEIGHT);
            helper.setCell(row, role.getDisplayValue(), 2);
            helper.setCell(row, ReportMath.calcListSummaryMinHoursWithoutQaAndPm(tasksByRole.get(role), request), 3);
            helper.setCell(row, ReportMath.calcListSummaryMinCostWithoutQaAndPm(tasksByRole.get(role), request), 4);
            helper.setCell(row, ReportMath.calcListSummaryMaxHoursWithoutQaAndPm(tasksByRole.get(role), request), 5);
            helper.setCell(row, ReportMath.calcListSummaryMaxCostWithoutQaAndPm(tasksByRole.get(role), request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (ReportMath.calcQaSummaryMaxHours(tasks, request) > 0) {
            Row row = createRow(ROW_HEIGHT);

            helper.setCell(row, "Специалист по тестированию", 2);
            helper.setCell(row, ReportMath.calcQaSummaryMinHours(tasks, request), 3);
            helper.setCell(row, ReportMath.calcQaSummaryMinCost(tasks, request), 4);
            helper.setCell(row, ReportMath.calcQaSummaryMaxHours(tasks, request), 5);
            helper.setCell(row, ReportMath.calcQaSummaryMaxCost(tasks, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (ReportMath.calcPmSummaryMaxHours(tasks, request) > 0) {
            Row row = createRow(ROW_HEIGHT);

            helper.setCell(row, "Руководитель", 2);
            helper.setCell(row, ReportMath.calcPmSummaryMinHours(tasks, request), 3);
            helper.setCell(row, ReportMath.calcPmSummaryMinCost(tasks, request), 4);
            helper.setCell(row, ReportMath.calcPmSummaryMaxHours(tasks, request), 5);
            helper.setCell(row, ReportMath.calcPmSummaryMaxCost(tasks, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }
    }

    private void fillSummary(TableType tableType) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        switch (tableType) {
            case DEFAULT_TABLE:
                helper.setTotalCell(row, "Итого по проекту:", 0);
                helper.setMarkedCell(row, hoursMinSummary, 3);
                helper.setMarkedCell(row, costMinSummary, 4);
                helper.setMarkedCell(row, hoursMaxSummary, 5);
                helper.setMarkedCell(row, costMaxSummary, 6);

                helper.setMarkedCell(row, null, 1);
                helper.setMarkedCell(row, null, 2);
                helper.setMarkedCell(row, null, 7);
                break;
            case LIGHT_TABLE:
                helper.setLightTotalCell(row, "Итого по проекту:", 0);
                helper.setLightCell(row, otherTasksMinHoursSummary, 3);
                helper.setLightCell(row, otherTasksMinCostSummary, 4);
                helper.setLightCell(row, otherTasksMaxHoursSummary, 5);
                helper.setLightCell(row, otherTasksMaxCostSummary, 6);

                helper.setLightCell(row, null, 1);
                helper.setLightCell(row, null, 2);
                helper.setLightCell(row, null, 7);
                break;
            case NONE:
                helper.setTotalCell(row, "Итого по проекту:", 0);
                helper.setMarkedCell(row, otherTasksMinHoursSummary + hoursMinSummary, 3);
                helper.setMarkedCell(row, otherTasksMinCostSummary + costMinSummary, 4);
                helper.setMarkedCell(row, otherTasksMaxHoursSummary + hoursMaxSummary, 5);
                helper.setMarkedCell(row, otherTasksMaxCostSummary + costMaxSummary, 6);

                helper.setMarkedCell(row, null, 1);
                helper.setMarkedCell(row, null, 2);
                helper.setMarkedCell(row, null, 7);
                break;
        }
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 12500);
        sheet.setColumnWidth(3, 4200);
        sheet.setColumnWidth(4, 4200);
        sheet.setColumnWidth(5, 4200);
        sheet.setColumnWidth(6, 4200);
        sheet.setColumnWidth(7, 12000);
    }
}

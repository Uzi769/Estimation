package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.enums.TableType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TasksByRolesSheet extends EstimationReportSheet {

    private double hoursMinSummary;
    private double costMinSummary;
    private double hoursMaxSummary;
    private double costMaxSummary;

    private double otherTasksMinHoursSummary;
    private double otherTasksMinCostSummary;
    private double otherTasksMaxHoursSummary;
    private double otherTasksMaxCostSummary;

    public TasksByRolesSheet(EstimationMath math, ExcelWorkbook helper) {
        super(math, helper);
    }

    @Override
    public void getSheet(Estimation estimation, Map<String, String> request) {
        sheet = helper.getWorkbook().createSheet(messageBundle.getString("sheetName.featureEstimation"));
        configureColumns();

        fillReportHeader(estimation, request, 7);

        fillTableHeader(messageBundle.getString("columnName.phases"), false);

        for (Phase phase : estimation.getPhases()) {
            List<Task> features = phase.getTasks().stream()
                    .filter(math::isFeature)
                    .collect(Collectors.toList());

            if (features.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, features, false);

            for (Task task : features) {
                fillFeatureRow(task, request);
            }
        }

        fillSummary(TableType.DEFAULT_TABLE, estimation, request);

        createRow(super.ROW_HEIGHT);
        fillTableHeader(messageBundle.getString("columnName.tasks"), true);

        for (Phase phase : estimation.getPhases()) {
            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> !math.isFeature(t))
                    .collect(Collectors.toList());

            if (tasks.isEmpty()) {
                continue;
            }

            fillPhaseRow(phase, request, tasks, true);

            for (Task task : tasks) {
                fillTaskRow(task, request);
            }
        }

        fillSummary(TableType.LIGHT_TABLE, estimation, request);

        createRow(ROW_HEIGHT);
        fillSummary(TableType.NONE, estimation, request);
    }

    private void fillTableHeader(String taskType, boolean isLight) {
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        if (isLight) {
            helper.setLightHeaderCell(row, taskType, 0);
            helper.setLightHeaderCell(row, messageBundle.getString("columnName.hoursMin"), 3);
            helper.setLightHeaderCell(row, messageBundle.getString("columnName.costMin"), 4);
            helper.setLightHeaderCell(row, messageBundle.getString("columnName.probableHours"), 5);
            helper.setLightHeaderCell(row, messageBundle.getString("columnName.probableCost"), 6);
            helper.setLightHeaderCell(row, messageBundle.getString("columnName.comment"), 7);

            helper.setLightHeaderCell(row, null, 1);
            helper.setLightHeaderCell(row, null, 2);
        } else {
            helper.setHeaderCell(row, taskType, 0);
            helper.setHeaderCell(row, messageBundle.getString("columnName.hoursMin"), 3);
            helper.setHeaderCell(row, messageBundle.getString("columnName.costMin"), 4);
            helper.setHeaderCell(row, messageBundle.getString("columnName.probableHours"), 5);
            helper.setHeaderCell(row, messageBundle.getString("columnName.probableCost"), 6);
            helper.setHeaderCell(row, messageBundle.getString("columnName.comment"), 7);

            helper.setHeaderCell(row, null, 1);
            helper.setHeaderCell(row, null, 2);
        }
    }

    private void fillPhaseRow(Phase phase, Map<String, String> request, List<Task> tasks, boolean isLight) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        double sumHoursMin = math.getListSummaryMinHours(tasks, request);
        double sumCostMin = math.getListSummaryMinCost(tasks, request);
        double sumHoursMax = math.getListSummaryMaxHours(tasks, request);
        double sumCostMax = math.getListSummaryMaxCost(tasks, request);

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

    private void fillFeatureRow(Task feature, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, math.getFeatureMinHours(feature, request), 3);
        helper.setCell(row, math.getFeatureMinCost(feature, request), 4);
        helper.setCell(row, math.getFeatureMaxHours(feature, request), 5);
        helper.setCell(row, math.getFeatureMaxCost(feature, request), 6);
        helper.setCell(row, feature.getComment(), 7);

        helper.setCell(row, null, 0);

        fillRoleRow(feature.getTasks(), request);
    }

    private void fillTaskRow(Task task, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setCell(row, task.getName(), 1);
        helper.setCell(row, math.getTaskMinHours(task, request), 3);
        helper.setCell(row, math.getTaskMinCost(task, request), 4);
        helper.setCell(row, math.getTaskMaxHours(task, request), 5);
        helper.setCell(row, math.getTaskMaxCost(task, request), 6);
        helper.setCell(row, task.getComment(), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 2);

        fillRoleRow(task, request);
    }

    private void fillRoleRow(Task task, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, task.getRole().getDisplayValue(), 2);
        helper.setCell(row, math.getTaskMinHoursWithoutQaAndPm(task, request), 3);
        helper.setCell(row, math.getTaskMinCostWithoutQaAndPm(task, request), 4);
        helper.setCell(row, math.getTaskMaxHoursWithoutQaAndPm(task, request), 5);
        helper.setCell(row, math.getTaskMaxCostWithoutQaAndPm(task, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);

        if (math.getQaMaxHours(task, request) > 0) {
            row = createRow(ROW_HEIGHT);

            helper.setCell(row, messageBundle.getString("cellName.tester"), 2);
            helper.setCell(row, math.getQaMinHours(task, request), 3);
            helper.setCell(row, math.getQaMinCost(task, request), 4);
            helper.setCell(row, math.getQaMaxHours(task, request), 5);
            helper.setCell(row, math.getQaMaxCost(task, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (math.getPmMaxHours(task, request) > 0) {
            row = createRow(ROW_HEIGHT);

            helper.setCell(row, messageBundle.getString("cellName.manager"), 2);
            helper.setCell(row, math.getPmMinHours(task, request), 3);
            helper.setCell(row, math.getPmMinCost(task, request), 4);
            helper.setCell(row, math.getPmMaxHours(task, request), 5);
            helper.setCell(row, math.getPmMaxCost(task, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }
    }

    private void fillRoleRow(List<Task> tasks, Map<String, String> request) {
        Map<Role, List<Task>> tasksByRole = tasks.stream().collect(Collectors.groupingBy(Task::getRole));

        for (Role role : tasksByRole.keySet()) {
            Row row = createRow(ROW_HEIGHT);
            helper.setCell(row, role.getDisplayValue(), 2);

            double minHours = math.getListSummaryMinHoursWithoutQaAndPm(tasksByRole.get(role), request);
            helper.setCell(row, minHours, 3);

            double minCost = math.getListSummaryMinCostWithoutQaAndPm(tasksByRole.get(role), request);
            helper.setCell(row, minCost, 4);

            double maxHours = math.getListSummaryMaxHoursWithoutQaAndPm(tasksByRole.get(role), request);
            helper.setCell(row, maxHours, 5);

            double maxCost = math.getListSummaryMaxCostWithoutQaAndPm(tasksByRole.get(role), request);
            helper.setCell(row, maxCost, 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (math.getQaSummaryMaxHours(tasks, request) > 0) {
            Row row = createRow(ROW_HEIGHT);

            helper.setCell(row, messageBundle.getString("cellName.tester"), 2);
            helper.setCell(row, math.getQaSummaryMinHours(tasks, request), 3);
            helper.setCell(row, math.getQaSummaryMinCost(tasks, request), 4);
            helper.setCell(row, math.getQaSummaryMaxHours(tasks, request), 5);
            helper.setCell(row, math.getQaSummaryMaxCost(tasks, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }

        if (math.getPmSummaryMaxHours(tasks, request) > 0) {
            Row row = createRow(ROW_HEIGHT);

            helper.setCell(row, messageBundle.getString("cellName.manager"), 2);
            helper.setCell(row, math.getPmSummaryMinHours(tasks, request), 3);
            helper.setCell(row, math.getPmSummaryMinCost(tasks, request), 4);
            helper.setCell(row, math.getPmSummaryMaxHours(tasks, request), 5);
            helper.setCell(row, math.getPmSummaryMaxCost(tasks, request), 6);

            helper.setCell(row, null, 0);
            helper.setCell(row, null, 1);
            helper.setCell(row, null, 7);
        }
    }

    private void fillSummary(TableType tableType, Estimation estimation, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        switch (tableType) {
            case DEFAULT_TABLE:
                helper.setTotalCell(row, messageBundle.getString("cellName.summary"), 0);
                helper.setMarkedCell(row, hoursMinSummary, 3);
                helper.setMarkedCell(row, costMinSummary, 4);
                helper.setMarkedCell(row, hoursMaxSummary, 5);
                helper.setMarkedCell(row, costMaxSummary, 6);

                helper.setMarkedCell(row, null, 1);
                helper.setMarkedCell(row, null, 2);
                helper.setMarkedCell(row, null, 7);
                break;
            case LIGHT_TABLE:
                helper.setLightTotalCell(row, messageBundle.getString("cellName.summary"), 0);
                helper.setLightCell(row, otherTasksMinHoursSummary, 3);
                helper.setLightCell(row, otherTasksMinCostSummary, 4);
                helper.setLightCell(row, otherTasksMaxHoursSummary, 5);
                helper.setLightCell(row, otherTasksMaxCostSummary, 6);

                helper.setLightCell(row, null, 1);
                helper.setLightCell(row, null, 2);
                helper.setLightCell(row, null, 7);
                break;
            case NONE:
                helper.setTotalCell(row, messageBundle.getString("cellName.summary"), 0);
                helper.setMarkedCell(row, math.getEstimationMinHours(estimation, request), 3);
                helper.setMarkedCell(row, math.getEstimationMinCost(estimation, request), 4);
                helper.setMarkedCell(row, math.getEstimationMaxHours(estimation, request), 5);
                helper.setMarkedCell(row, math.getEstimationMaxCost(estimation, request), 6);

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

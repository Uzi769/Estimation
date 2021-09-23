package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstimationWithDetailsSheet extends EstimationReportSheet {

    public EstimationWithDetailsSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, Map<String, String> request) {
        sheet = helper.getWorkbook().createSheet(messageBundle.getString("sheetName.withDetails"));
        configureColumns();

        fillReportHeader(estimation, request, 8);

        fillTableHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(phase, request);

            List<Task> notNestedTask = new ArrayList<>();
            for (Task task : phase.getTasks()) {
                if (EntitiesIdConstants.FEATURE_ID.equals(task.getType().getId())) {
                    fillFeatureRowWithNestedTasks(task, request);
                } else if (EntitiesIdConstants.TASK_ID.equals(task.getType().getId())) {
                    notNestedTask.add(task);
                }
            }

            notNestedTask.forEach(task -> fillTaskRow(task, request, 1));
            fillQaAndPmRows(notNestedTask, request, 1);
        }

        fillSummary(estimation, request);
    }

    private void fillTableHeader() {
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setHeaderCell(row, messageBundle.getString("columnName.tasks"), 0);
        helper.setHeaderCell(row, messageBundle.getString("columnName.specialist"), 3);
        helper.setHeaderCell(row, messageBundle.getString("columnName.hoursMin"), 4);
        helper.setHeaderCell(row, messageBundle.getString("columnName.costMin"), 5);
        helper.setHeaderCell(row, messageBundle.getString("columnName.probableHours"), 6);
        helper.setHeaderCell(row, messageBundle.getString("columnName.probableCost"), 7);
        helper.setHeaderCell(row, messageBundle.getString("columnName.comment"), 8);

        helper.setHeaderCell(row, null, 1);
        helper.setHeaderCell(row, null, 2);
    }

    private void fillPhaseRow(Phase phase, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);
        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);

        helper.setMarkedCell(row, null, 3);

        double sumHoursMin = EstimationMath.calcListSummaryMinHours(phase.getTasks(), request);
        helper.setMarkedCell(row, sumHoursMin, 4);

        double sumCostMin = EstimationMath.calcListSummaryMinCost(phase.getTasks(), request);
        helper.setMarkedCell(row, sumCostMin, 5);

        double sumHoursMax = EstimationMath.calcListSummaryMaxHours(phase.getTasks(), request);
        helper.setMarkedCell(row, sumHoursMax, 6);

        double sumCostMax = EstimationMath.calcListSummaryMaxCost(phase.getTasks(), request);
        helper.setMarkedCell(row, sumCostMax, 7);

        helper.setMarkedCell(row, null, 8);
    }

    private void fillTaskRow(Task task, Map<String, String> request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, task.getName(), column);
        helper.setCell(row, task.getRole() != null ? task.getRole().getDisplayValue() : null, 3);
        helper.setCell(row, EstimationMath.calcTaskMinHours(task, request), 4);
        helper.setCell(row, EstimationMath.calcTaskMinCost(task, request), 5);
        helper.setCell(row, EstimationMath.calcTaskMaxHours(task, request), 6);
        helper.setCell(row, EstimationMath.calcTaskMaxCost(task, request), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, task.getComment(), 8);
    }

    private void fillFeatureRowWithNestedTasks(Task feature, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, null, 0);
        helper.setBoldCell(row, feature.getName(), 1);
        helper.setBoldCell(row, null, 2);
        helper.setBoldCell(row, EstimationMath.calcFeatureMinHours(feature, request), 4);
        helper.setBoldCell(row, EstimationMath.calcFeatureMinCost(feature, request), 5);
        helper.setBoldCell(row, EstimationMath.calcFeatureMaxHours(feature, request), 6);
        helper.setBoldCell(row, EstimationMath.calcFeatureMaxCost(feature, request), 7);
        helper.setBoldCell(row, feature.getComment(), 8);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask, request, 2);
        }

        fillQaAndPmRows(feature.getTasks(), request, 2);
    }

    private void fillQaAndPmRows(List<Task> tasks, Map<String, String> request, int column) {
        if (EstimationMath.calcQaSummaryMaxHours(tasks, request) > 0) {
            fillQaRow(tasks, request, column);
        }

        if (EstimationMath.calcPmSummaryMaxHours(tasks, request) > 0) {
            fillPmRow(tasks, request, column);
        }
    }

    private void fillQaRow(List<Task> tasks, Map<String, String> request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, null, 0);
        helper.setCell(row, messageBundle.getString("cellName.testing"), column);
        helper.setCell(row, messageBundle.getString("cellName.tester"), 3);
        helper.setCell(row, EstimationMath.calcQaSummaryMinHours(tasks, request), 4);
        helper.setCell(row, EstimationMath.calcQaSummaryMinCost(tasks, request), 5);
        helper.setCell(row, EstimationMath.calcQaSummaryMaxHours(tasks, request), 6);
        helper.setCell(row, EstimationMath.calcQaSummaryMaxCost(tasks, request), 7);
        helper.setCell(row, null, 8);
    }

    private void fillPmRow(List<Task> tasks, Map<String, String> request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, messageBundle.getString("cellName.management"), column);
        helper.setCell(row, messageBundle.getString("cellName.projectManager"), 3);
        helper.setCell(row, EstimationMath.calcPmSummaryMinHours(tasks, request), 4);
        helper.setCell(row, EstimationMath.calcPmSummaryMinCost(tasks, request), 5);
        helper.setCell(row, EstimationMath.calcPmSummaryMaxHours(tasks, request), 6);
        helper.setCell(row, EstimationMath.calcPmSummaryMaxCost(tasks, request), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 8);
    }

    private void fillSummary(Estimation estimation, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 3);

        helper.setTotalCell(row, messageBundle.getString("cellName.summary"), 0);
        helper.setMarkedCell(row, EstimationMath.calcEstimationMinHours(estimation, request), 4);
        helper.setMarkedCell(row, EstimationMath.calcEstimationMinCost(estimation, request), 5);
        helper.setMarkedCell(row, EstimationMath.calcEstimationMaxHours(estimation, request), 6);
        helper.setMarkedCell(row, EstimationMath.calcEstimationMaxCost(estimation, request), 7);

        helper.setTotalCell(row, null, 1);
        helper.setTotalCell(row, null, 2);
        helper.setTotalCell(row, null, 3);
        helper.setMarkedCell(row, null, 8);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 12500);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 4200);
        sheet.setColumnWidth(5, 4200);
        sheet.setColumnWidth(6, 4200);
        sheet.setColumnWidth(7, 4200);
        sheet.setColumnWidth(8, 12000);
    }
}

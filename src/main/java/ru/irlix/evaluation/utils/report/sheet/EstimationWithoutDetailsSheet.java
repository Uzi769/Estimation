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

public class EstimationWithoutDetailsSheet extends EstimationReportSheet {

    public EstimationWithoutDetailsSheet(EstimationMath math, ExcelWorkbook workbook) {
        super(math, workbook);
    }

    @Override
    public void getSheet(Estimation estimation, Map<String, String> request) {
        sheet = helper.getWorkbook().createSheet(messageBundle.getString("sheetName.withoutDetails"));
        configureColumns();

        fillReportHeader(estimation, request, 7);

        fillTableHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(phase, request);

            List<Task> notNestedTasks = new ArrayList<>();
            for (Task task : phase.getTasks()) {
                if (EntitiesIdConstants.FEATURE_ID.equals(task.getType().getId())) {
                    fillFeatureRowWithNestedTasks(task, request);
                } else if (EntitiesIdConstants.TASK_ID.equals(task.getType().getId())) {
                    notNestedTasks.add(task);
                }
            }

            notNestedTasks.forEach(task -> fillTaskRow(task, request, 1));
        }

        fillSummary(estimation, request);
    }

    private void fillTableHeader() {
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setHeaderCell(row, messageBundle.getString("columnName.tasks"), 0);
        helper.setHeaderCell(row, messageBundle.getString("columnName.hoursMin"), 3);
        helper.setHeaderCell(row, messageBundle.getString("columnName.costMin"), 4);
        helper.setHeaderCell(row, messageBundle.getString("columnName.probableHours"), 5);
        helper.setHeaderCell(row, messageBundle.getString("columnName.probableCost"), 6);
        helper.setHeaderCell(row, messageBundle.getString("columnName.comment"), 7);

        helper.setHeaderCell(row, null, 1);
        helper.setHeaderCell(row, null, 2);
    }

    private void fillPhaseRow(Phase phase, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);

        double sumHoursMin = math.getListSummaryMinHours(phase.getTasks(), request);
        helper.setMarkedCell(row, sumHoursMin, 3);

        double sumCostMin = math.getListSummaryMinCost(phase.getTasks(), request);
        helper.setMarkedCell(row, sumCostMin, 4);

        double sumHoursMax = math.getListSummaryMaxHours(phase.getTasks(), request);
        helper.setMarkedCell(row, sumHoursMax, 5);

        double sumCostMax = math.getListSummaryMaxCost(phase.getTasks(), request);
        helper.setMarkedCell(row, sumCostMax, 6);

        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);
        helper.setMarkedCell(row, null, 7);
    }

    private void fillTaskRow(Task task, Map<String, String> request, int column) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, null, 0);

        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, task.getName(), column);
            helper.setCell(row, null, 2);
            helper.setCell(row, math.getTaskMinHours(task, request), 3);
            helper.setCell(row, math.getTaskMinCost(task, request), 4);
            helper.setCell(row, math.getTaskMaxHours(task, request), 5);
            helper.setCell(row, math.getTaskMaxCost(task, request), 6);
            helper.setCell(row, task.getComment(), 7);
        } else {
            helper.setCell(row, null, 1);
            helper.setCell(row, task.getName(), column);
            helper.setCell(row, null, 3);
            helper.setCell(row, null, 4);
            helper.setCell(row, null, 5);
            helper.setCell(row, null, 6);
            helper.setCell(row, null, 7);
        }
    }

    private void fillFeatureRowWithNestedTasks(Task feature, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, null, 0);
        helper.setBoldCell(row, feature.getName(), 1);
        helper.setBoldCell(row, null, 2);
        helper.setBoldCell(row, math.getFeatureMinHours(feature, request), 3);
        helper.setBoldCell(row, math.getFeatureMinCost(feature, request), 4);
        helper.setBoldCell(row, math.getFeatureMaxHours(feature, request), 5);
        helper.setBoldCell(row, math.getFeatureMaxCost(feature, request), 6);
        helper.setBoldCell(row, feature.getComment(), 7);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask, request, 2);
        }

        fillQaAndPmRows(feature.getTasks(), request);
    }

    private void fillQaAndPmRows(List<Task> tasks, Map<String, String> request) {
        if (math.getQaSummaryMaxHours(tasks, request) > 0) {
            fillQaRow(tasks, request);
        }

        if (math.getPmSummaryMaxHours(tasks, request) > 0) {
            fillPmRow(tasks, request);
        }
    }

    private void fillQaRow(List<Task> tasks, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, messageBundle.getString("cellName.testing"), 2);
        helper.setCell(row, math.getQaSummaryMinHours(tasks, request), 3);
        helper.setCell(row, math.getQaSummaryMinCost(tasks, request), 4);
        helper.setCell(row, math.getQaSummaryMaxHours(tasks, request), 5);
        helper.setCell(row, math.getQaSummaryMaxCost(tasks, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);
    }

    private void fillPmRow(List<Task> tasks, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, messageBundle.getString("cellName.management"), 2);
        helper.setCell(row, math.getPmSummaryMinHours(tasks, request), 3);
        helper.setCell(row, math.getPmSummaryMinCost(tasks, request), 4);
        helper.setCell(row, math.getPmSummaryMaxHours(tasks, request), 5);
        helper.setCell(row, math.getPmSummaryMaxCost(tasks, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);
    }

    private void fillSummary(Estimation estimation, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setTotalCell(row, messageBundle.getString("cellName.summary"), 0);
        helper.setMarkedCell(row, math.getEstimationMinHours(estimation, request), 3);
        helper.setMarkedCell(row, math.getEstimationMinCost(estimation, request), 4);
        helper.setMarkedCell(row, math.getEstimationMaxHours(estimation, request), 5);
        helper.setMarkedCell(row, math.getEstimationMaxCost(estimation, request), 6);

        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);
        helper.setMarkedCell(row, null, 7);
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

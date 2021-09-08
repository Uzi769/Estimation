package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.util.ArrayList;
import java.util.List;

public class EstimationWithoutDetailsSheet extends EstimationReportSheet {

    public EstimationWithoutDetailsSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка без детализации");
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

        fillSummary();
    }

    private void fillTableHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 4);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);

        helper.setHeaderCell(row, null, 1);
        helper.setHeaderCell(row, null, 2);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);

        double sumHoursMin = ReportMath.calcListSummaryMinHours(phase.getTasks(), request);
        hoursMinSummary += sumHoursMin;
        helper.setMarkedCell(row, sumHoursMin, 3);

        double sumCostMin = ReportMath.calcListSummaryMinCost(phase.getTasks(), request);
        costMinSummary += sumCostMin;
        helper.setMarkedCell(row, sumCostMin, 4);

        double sumHoursMax = ReportMath.calcListSummaryMaxHours(phase.getTasks(), request);
        hoursMaxSummary += sumHoursMax;
        helper.setMarkedCell(row, sumHoursMax, 5);

        double sumCostMax = ReportMath.calcListSummaryMaxCost(phase.getTasks(), request);
        costMaxSummary += sumCostMax;
        helper.setMarkedCell(row, sumCostMax, 6);

        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);
        helper.setMarkedCell(row, null, 7);
    }

    private void fillTaskRow(Task task, ReportRequest request, int column) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, null, 0);

        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, task.getName(), column);
            helper.setCell(row, null, 2);
            helper.setCell(row, ReportMath.calcTaskMinHoursWithQaAndPm(task, request), 3);
            helper.setCell(row, ReportMath.calcTaskMinCostWithQaAndPm(task, request), 4);
            helper.setCell(row, ReportMath.calcTaskMaxHoursWithQaAndPm(task, request), 5);
            helper.setCell(row, ReportMath.calcTaskMaxCostWithQaAndPm(task, request), 6);
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

    private void fillFeatureRowWithNestedTasks(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, null, 0);
        helper.setBoldCell(row, feature.getName(), 1);
        helper.setBoldCell(row, null, 2);
        helper.setBoldCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setBoldCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setBoldCell(row, feature.getComment(), 7);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask, request, 2);
        }

        fillQaAndPmRows(feature.getTasks(), request);
    }

    private void fillQaAndPmRows(List<Task> tasks, ReportRequest request) {
        if (ReportMath.calcQaSummaryMaxHours(tasks, request) > 0) {
            fillQaRow(tasks, request);
        }

        if (ReportMath.calcPmSummaryMaxHours(tasks, request) > 0) {
            fillPmRow(tasks, request);
        }
    }

    private void fillQaRow(List<Task> tasks, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, "Тестирование", 2);
        helper.setCell(row, ReportMath.calcQaSummaryMinHours(tasks, request), 3);
        helper.setCell(row, ReportMath.calcQaSummaryMinCost(tasks, request), 4);
        helper.setCell(row, ReportMath.calcQaSummaryMaxHours(tasks, request), 5);
        helper.setCell(row, ReportMath.calcQaSummaryMaxCost(tasks, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);
    }

    private void fillPmRow(List<Task> tasks, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, "Управление", 2);
        helper.setCell(row, ReportMath.calcPmSummaryMinHours(tasks, request), 3);
        helper.setCell(row, ReportMath.calcPmSummaryMinCost(tasks, request), 4);
        helper.setCell(row, ReportMath.calcPmSummaryMaxHours(tasks, request), 5);
        helper.setCell(row, ReportMath.calcPmSummaryMaxCost(tasks, request), 6);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 1);
        helper.setCell(row, null, 7);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursMinSummary, 3);
        helper.setMarkedCell(row, costMinSummary, 4);
        helper.setMarkedCell(row, hoursMaxSummary, 5);
        helper.setMarkedCell(row, costMaxSummary, 6);

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

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

public class EstimationWithDetailsSheet extends EstimationReportSheet {

    public EstimationWithDetailsSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка с детализацией");
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

        fillSummary();
    }

    private void fillTableHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Специалист", 3);
        helper.setHeaderCell(row, "Часы (мин)", 4);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 5);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 6);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 7);
        helper.setHeaderCell(row, "Комментарии", 8);

        helper.setHeaderCell(row, null, 1);
        helper.setHeaderCell(row, null, 2);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);
        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);

        helper.setMarkedCell(row, null, 3);

        double sumHoursMin = ReportMath.calcListSummaryMinHours(phase.getTasks(), request);
        hoursMinSummary += sumHoursMin;
        helper.setMarkedCell(row, sumHoursMin, 4);

        double sumCostMin = ReportMath.calcListSummaryMinCost(phase.getTasks(), request);
        costMinSummary += sumCostMin;
        helper.setMarkedCell(row, sumCostMin, 5);

        double sumHoursMax = ReportMath.calcListSummaryMaxHours(phase.getTasks(), request);
        hoursMaxSummary += sumHoursMax;
        helper.setMarkedCell(row, sumHoursMax, 6);

        double sumCostMax = ReportMath.calcListSummaryMaxCost(phase.getTasks(), request);
        costMaxSummary += sumCostMax;
        helper.setMarkedCell(row, sumCostMax, 7);

        helper.setMarkedCell(row, null, 8);
    }

    private void fillTaskRow(Task task, ReportRequest request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, task.getName(), column);
        helper.setCell(row, task.getRole() != null ? task.getRole().getDisplayValue() : null, 3);
        helper.setCell(row, ReportMath.calcTaskMinHours(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMinCost(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxHours(task, request), 6);
        helper.setCell(row, ReportMath.calcTaskMaxCost(task, request), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, task.getComment(), 8);
    }

    private void fillFeatureRowWithNestedTasks(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, null, 0);
        helper.setBoldCell(row, feature.getName(), 1);
        helper.setBoldCell(row, null, 2);
        helper.setBoldCell(row, ReportMath.calcFeatureMinHours(feature, request), 4);
        helper.setBoldCell(row, ReportMath.calcFeatureMinCost(feature, request), 5);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxHours(feature, request), 6);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxCost(feature, request), 7);
        helper.setBoldCell(row, feature.getComment(), 8);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask, request, 2);
        }

        fillQaAndPmRows(feature.getTasks(), request, 2);
    }

    private void fillQaAndPmRows(List<Task> tasks, ReportRequest request, int column) {
        if (ReportMath.calcQaSummaryMaxHours(tasks, request) > 0) {
            fillQaRow(tasks, request, column);
        }

        if (ReportMath.calcPmSummaryMaxHours(tasks, request) > 0) {
            fillPmRow(tasks, request, column);
        }
    }

    private void fillQaRow(List<Task> tasks, ReportRequest request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, null, 0);
        helper.setCell(row, "Тестирование", column);
        helper.setCell(row, "Специалист по тестированию", 3);
        helper.setCell(row, ReportMath.calcQaSummaryMinHours(tasks, request), 4);
        helper.setCell(row, ReportMath.calcQaSummaryMinCost(tasks, request), 5);
        helper.setCell(row, ReportMath.calcQaSummaryMaxHours(tasks, request), 6);
        helper.setCell(row, ReportMath.calcQaSummaryMaxCost(tasks, request), 7);
        helper.setCell(row, null, 8);
    }

    private void fillPmRow(List<Task> tasks, ReportRequest request, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(column, 2);
            helper.setCell(row, null, 2);
        } else {
            helper.setCell(row, null, 1);
        }

        helper.setCell(row, "Управление", column);
        helper.setCell(row, "Руководитель проекта", 3);
        helper.setCell(row, ReportMath.calcPmSummaryMinHours(tasks, request), 4);
        helper.setCell(row, ReportMath.calcPmSummaryMinCost(tasks, request), 5);
        helper.setCell(row, ReportMath.calcPmSummaryMaxHours(tasks, request), 6);
        helper.setCell(row, ReportMath.calcPmSummaryMaxCost(tasks, request), 7);

        helper.setCell(row, null, 0);
        helper.setCell(row, null, 8);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 3);

        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursMinSummary, 4);
        helper.setMarkedCell(row, costMinSummary, 5);
        helper.setMarkedCell(row, hoursMaxSummary, 6);
        helper.setMarkedCell(row, costMaxSummary, 7);

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

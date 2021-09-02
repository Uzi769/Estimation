package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FeatureSheet implements Sheet {

    private final ExcelWorkbook helper;
    private XSSFSheet sheet;

    private double hoursMinSummary;
    private double hoursMaxSummary;
    private double costMinSummary;
    private double costMaxSummary;

    private final short ROW_HEIGHT = 380;

    private int rowNum = 0;

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка по фичам");
        configureColumns();

        fillHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(phase, request);

            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> t.getParent() == null)
                    .collect(Collectors.toList());

            List<Task> otherTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (EntityConstants.FEATURE_ID.equals(task.getType().getId())) {
                    fillFeatureRowWithNestedTasks(task, request);
                } else if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
                    otherTasks.add(task);
                }
            }

            if (!otherTasks.isEmpty()) {
                fillOtherTasksRow(otherTasks, request);
            }
        }

        fillSummary();
    }

    private void fillHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(0);

        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 4);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(0);

        helper.setMarkedCell(row, phase.getName(), 0);

        double sumHoursMin = ReportMath.calcPhaseSummaryMinHours(phase, request);
        hoursMinSummary += sumHoursMin;
        helper.setMarkedCell(row, sumHoursMin, 3);

        double sumCostMin = ReportMath.calcPhaseSummaryMinCost(phase, request);
        costMinSummary += sumCostMin;
        helper.setMarkedCell(row, sumCostMin, 4);

        double sumHoursMax = ReportMath.calcPhaseSummaryMaxHours(phase, request);
        hoursMaxSummary += sumHoursMax;
        helper.setMarkedCell(row, sumHoursMax, 5);

        double sumCostMax = ReportMath.calcPhaseSummaryMaxCost(phase, request);
        costMaxSummary += sumCostMax;
        helper.setMarkedCell(row, sumCostMax, 6);

        helper.setMarkedCell(row, null, 7);
    }

    private void fillOtherTasksRow(List<Task> otherTasks, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(1);

        helper.setBoldCell(row, "Прочие задачи", 1);
        helper.setCell(row, ReportMath.calcListSummaryMinHours(otherTasks, request), 3);
        helper.setCell(row, ReportMath.calcListSummaryMinCost(otherTasks, request), 4);
        helper.setCell(row, ReportMath.calcListSummaryMaxHours(otherTasks, request), 5);
        helper.setCell(row, ReportMath.calcListSummaryMaxCost(otherTasks, request), 6);
    }

    private void fillFeatureRowWithNestedTasks(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(1);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setCell(row, feature.getComment(), 8);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(0);

        helper.setTotalCell(row, "Итого по проекту:", 0);
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

    private Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;

        return row;
    }

    private void mergeCellsToSecondColumnInclude(int startColumn) {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, 2));
    }
}

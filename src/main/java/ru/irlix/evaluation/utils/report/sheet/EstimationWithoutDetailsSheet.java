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
import ru.irlix.evaluation.utils.report.ReportMath;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EstimationWithoutDetailsSheet implements Sheet {

    private final ExcelWorkbook helper;
    private double hoursMinSummary;
    private double hoursMaxSummary;
    private double costMinSummary;
    private double costMaxSummary;

    private XSSFSheet sheet;
    private int rowNum = 0;
    private final short ROW_HEIGHT = 380;

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка без детализации");
        configure();
        fillHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(request, phase);

            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> t.getParent() == null)
                    .collect(Collectors.toList());

            for (Task task : tasks) {
                if (EntityConstants.FEATURE_ID.equals(task.getType().getId())) {
                    fillFeatureRowWithNestedTasks(request, task);
                } else if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
                    fillTaskRow(request, task, 1);
                }
            }
        }
        fillSummary();
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);
        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setPhaseCell(row, hoursMinSummary, 3);
        helper.setPhaseCell(row, costMinSummary, 4);
        helper.setPhaseCell(row, hoursMaxSummary, 5);
        helper.setPhaseCell(row, costMaxSummary, 6);
        helper.setPhaseCell(row, null, 7);
    }

    private void fillFeatureRowWithNestedTasks(ReportRequest request, Task feature) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);
        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setCell(row, feature.getComment(), 7);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(request, nestedTask, 2);
        }
    }

    private void fillPhaseRow(ReportRequest request, Phase phase) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);
        helper.setPhaseCell(row, phase.getName(), 0);

        double sumHoursMin = ReportMath.calcPhaseSummaryMinHours(phase, request);
        hoursMinSummary += sumHoursMin;
        helper.setPhaseCell(row, sumHoursMin, 3);

        double sumCostMin = ReportMath.calcPhaseSummaryMinCost(phase, request);
        costMinSummary += sumCostMin;
        helper.setPhaseCell(row, sumCostMin, 4);

        double sumHoursMax = ReportMath.calcPhaseSummaryMaxHours(phase, request);
        hoursMaxSummary += sumHoursMax;
        helper.setPhaseCell(row, sumHoursMax, 5);

        double sumCostMax = ReportMath.calcPhaseSummaryMaxCost(phase, request);
        costMaxSummary += sumCostMax;
        helper.setPhaseCell(row, sumCostMax, 6);

        helper.setPhaseCell(row, null, 7);
    }

    private void fillHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 2);
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость, RUB", 4);
        helper.setHeaderCell(row, "Часы (мин, наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);
    }

    private void configure() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 12000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 10000);
    }

    private void mergeCells(int startColumn, int endColumn) {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, endColumn));
    }

    private Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;
        return row;
    }

    private void fillTaskRow(ReportRequest request, Task task, int column) {
        Row row = createRow(ROW_HEIGHT);
        if (column == 1) {
            mergeCells(1, 2);
        }
        helper.setCell(row, task.getName(), column);
        helper.setCell(row, ReportMath.calcTaskMinHours(task, request), 3);
        helper.setCell(row, ReportMath.calcTaskMinCost(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMaxHours(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxCost(task, request), 6);
        helper.setCell(row, task.getComment(), 7);
    }
}

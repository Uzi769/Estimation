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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EstimationWithoutDetailsSheet implements Sheet {

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
        sheet = helper.getWorkbook().createSheet("Оценка без детализации");
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
                fillOtherTasksRow(otherTasks);
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

    private void fillTaskRow(Task task) {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, task.getName(), 2);
    }

    private void fillOtherTasksRow(List<Task> otherTasks) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(1);

        helper.setBoldCell(row, "Прочие задачи", 1);

        for (Task task : otherTasks) {
            fillTaskRow(task);
        }

        fillQaAndPmRows(otherTasks);
    }

    private void fillFeatureRowWithNestedTasks(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondColumnInclude(1);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setBoldCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setBoldCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setBoldCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setBoldCell(row, feature.getComment(), 7);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask);
        }

        fillQaAndPmRows(feature.getTasks());
    }

    private void fillQaAndPmRows(List<Task> tasks) {
        if (ReportMath.calcQaSummaryMaxHours(tasks) > 0) {
            fillQaRow();
        }

        if (ReportMath.calcPmSummaryMaxHours(tasks) > 0) {
            fillPmRow();
        }
    }

    private void fillQaRow() {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, "Тестирование", 2);
    }

    private void fillPmRow() {
        Row row = createRow(ROW_HEIGHT);
        helper.setCell(row, "Управление", 2);
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

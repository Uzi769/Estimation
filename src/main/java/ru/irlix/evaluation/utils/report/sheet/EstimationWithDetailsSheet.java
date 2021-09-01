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
public class EstimationWithDetailsSheet implements Sheet {

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
        sheet = helper.getWorkbook().createSheet("Оценка с детализацией");
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
        mergeCells(0, 2);

        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Специалист", 3);
        helper.setHeaderCell(row, "Часы (мин)", 4);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 5);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 6);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 7);
        helper.setHeaderCell(row, "Комментарии", 8);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 2);

        helper.setMarkedCell(row, phase.getName(), 0);
        helper.setMarkedCell(row, null, 3);

        double sumHoursMin = ReportMath.calcPhaseSummaryMinHours(phase, request);
        hoursMinSummary += sumHoursMin;
        helper.setMarkedCell(row, sumHoursMin, 4);

        double sumCostMin = ReportMath.calcPhaseSummaryMinCost(phase, request);
        costMinSummary += sumCostMin;
        helper.setMarkedCell(row, sumCostMin, 5);

        double sumHoursMax = ReportMath.calcPhaseSummaryMaxHours(phase, request);
        hoursMaxSummary += sumHoursMax;
        helper.setMarkedCell(row, sumHoursMax, 6);

        double sumCostMax = ReportMath.calcPhaseSummaryMaxCost(phase, request);
        costMaxSummary += sumCostMax;
        helper.setMarkedCell(row, sumCostMax, 7);

        helper.setMarkedCell(row, null, 8);
    }

    private void fillOtherTasksRow(List<Task> otherTasks, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, "Прочие задачи", 1);
        helper.setCell(row, ReportMath.calcListSummaryMinHours(otherTasks, request), 4);
        helper.setCell(row, ReportMath.calcListSummaryMinCost(otherTasks, request), 5);
        helper.setCell(row, ReportMath.calcListSummaryMaxHours(otherTasks, request), 6);
        helper.setCell(row, ReportMath.calcListSummaryMaxCost(otherTasks, request), 7);

        for (Task task : otherTasks) {
            fillTaskRow(task, request);
        }
    }

    private void fillTaskRow(Task task, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, task.getName(), 2);
        helper.setCell(row, task.getRole() != null ? task.getRole().getDisplayValue() : null, 3);
        helper.setCell(row, ReportMath.calcTaskMinHours(task, request), 4);
        helper.setCell(row, ReportMath.calcTaskMinCost(task, request), 5);
        helper.setCell(row, ReportMath.calcTaskMaxHours(task, request), 6);
        helper.setCell(row, ReportMath.calcTaskMaxCost(task, request), 7);
        helper.setCell(row, task.getComment(), 8);
    }

    private void fillFeatureRowWithNestedTasks(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(1, 2);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, ReportMath.calcFeatureMinHours(feature, request), 4);
        helper.setCell(row, ReportMath.calcFeatureMinCost(feature, request), 5);
        helper.setCell(row, ReportMath.calcFeatureMaxHours(feature, request), 6);
        helper.setCell(row, ReportMath.calcFeatureMaxCost(feature, request), 7);
        helper.setCell(row, feature.getComment(), 8);

        for (Task nestedTask : feature.getTasks()) {
            fillTaskRow(nestedTask, request);
        }
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 3);

        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursMinSummary, 4);
        helper.setMarkedCell(row, costMinSummary, 5);
        helper.setMarkedCell(row, hoursMaxSummary, 6);
        helper.setMarkedCell(row, costMaxSummary, 7);
        helper.setMarkedCell(row, null, 8);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 16000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 12000);
    }

    private Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;

        return row;
    }

    private void mergeCells(int startColumn, int endColumn) {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, endColumn));
    }
}

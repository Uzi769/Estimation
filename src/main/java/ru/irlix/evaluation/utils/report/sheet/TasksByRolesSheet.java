package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.ReportMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TasksByRolesSheet implements Sheet {

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
        sheet = helper.getWorkbook().createSheet("Task by roles");
        configureColumns();

        fillHeader();

        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases().forEach(p -> allTasks.addAll(p.getTasks()));

        Map<Role, List<Task>> roleMap = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getRole));

        for (Map.Entry<Role, List<Task>> entry : roleMap.entrySet()) {
            Row roleRow = fillRoleRow(entry, request);

            List<Task> otherTasks = new ArrayList<>();
            for (Task task : entry.getValue()) {
                if (EntityConstants.FEATURE_ID.equals(task.getType().getId())) {
                    fillFeatureRow(task, request);
                } else {
                    otherTasks.add(task);
                }
            }

            if (entry.getValue().isEmpty()) {
                sheet.removeRow(roleRow);
                rowNum--;
                continue;
            }

            fillOtherTaskRow(otherTasks, request);
        }

        fillSummary();
    }

    private void fillHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCellsToSecondCol(0);

        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 4);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);
    }

    private Row fillRoleRow(Map.Entry<Role, List<Task>> roleEntry, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondCol(0);

        helper.setPhaseCell(row, roleEntry.getKey().getDisplayValue(), 0);

        double sumHoursMin = ReportMath.calcListSummaryMinHours(roleEntry.getValue(), request);
        hoursMinSummary += sumHoursMin;
        helper.setPhaseCell(row, sumHoursMin, 3);

        double sumCostMin = ReportMath.calcListSummaryMinCost(roleEntry.getValue(), request);
        costMinSummary += sumCostMin;
        helper.setPhaseCell(row, sumCostMin, 4);

        double sumHoursMax = ReportMath.calcListSummaryMaxHours(roleEntry.getValue(), request);
        hoursMaxSummary += sumHoursMax;
        helper.setPhaseCell(row, sumHoursMax, 5);

        double sumCostMax = ReportMath.calcListSummaryMaxCost(roleEntry.getValue(), request);
        costMaxSummary += sumCostMax;
        helper.setPhaseCell(row, sumCostMax, 6);

        helper.setPhaseCell(row, null, 7);

        return row;
    }

    private void fillOtherTaskRow(List<Task> otherTasks, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondCol(1);

        helper.setCell(row, "Прочие задачи", 1);
        helper.setCell(row, ReportMath.calcListSummaryMinHours(otherTasks, request), 3);
        helper.setCell(row, ReportMath.calcListSummaryMinCost(otherTasks, request), 4);
        helper.setCell(row, ReportMath.calcListSummaryMaxHours(otherTasks, request), 5);
        helper.setCell(row, ReportMath.calcListSummaryMaxCost(otherTasks, request), 6);
        helper.setCell(row, null, 7);
    }

    private void fillFeatureRow(Task feature, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondCol(1);

        helper.setBoldCell(row, feature.getName(), 1);
        helper.setCell(row, ReportMath.calcFeatureMinHours(feature, request), 3);
        helper.setCell(row, ReportMath.calcFeatureMinCost(feature, request), 4);
        helper.setCell(row, ReportMath.calcFeatureMaxHours(feature, request), 5);
        helper.setCell(row, ReportMath.calcFeatureMaxCost(feature, request), 6);
        helper.setCell(row, feature.getComment(), 7);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeCellsToSecondCol(0);

        helper.setPhaseCell(row, "Итого по проекту:", 0);
        helper.setPhaseCell(row, hoursMinSummary, 3);
        helper.setPhaseCell(row, costMinSummary, 4);
        helper.setPhaseCell(row, hoursMaxSummary, 5);
        helper.setPhaseCell(row, costMaxSummary, 6);
        helper.setPhaseCell(row, null, 7);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 16000);
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

    private void mergeCellsToSecondCol(int startColumn) {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, 2));
    }
}

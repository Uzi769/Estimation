package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.Map;

public class PhaseEstimationSheet extends EstimationReportSheet {

    public PhaseEstimationSheet(EstimationMath math, ExcelWorkbook helper) {
        super(math, helper);
    }

    @Override
    public void getSheet(Estimation estimation, Map<String, String> request) {
        sheet = helper.getWorkbook().createSheet(messageBundle.getString("sheetName.phaseEstimation"));
        configureColumns();

        fillReportHeader(estimation, request, 7);

        fillTableHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(phase, request);
        }

        fillSummary(estimation, request);
    }

    private void fillTableHeader() {
        Row row = createRow(HEADER_ROW_HEIGHT);
        mergeCells(0, 3);
        mergeCells(4, 6);

        helper.setHeaderCell(row, messageBundle.getString("columnName.phases"), 0);
        helper.setHeaderCell(row, messageBundle.getString("columnName.hours"), 4);
        helper.setHeaderCell(row, messageBundle.getString("columnName.cost"), 7);

        helper.setHeaderCell(row, null, 1);
        helper.setHeaderCell(row, null, 2);
        helper.setHeaderCell(row, null, 3);
        helper.setHeaderCell(row, null, 5);
        helper.setHeaderCell(row, null, 6);
    }

    private void fillPhaseRow(Phase phase, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 3);
        mergeCells(4, 6);

        helper.setCell(row, phase.getName(), 0);

        double sumHoursMax = math.getListSummaryMaxHours(phase.getTasks(), request);
        helper.setCell(row, sumHoursMax, 4);

        double sumCostMax = math.getListSummaryMaxCost(phase.getTasks(), request);
        helper.setCell(row, sumCostMax, 7);

        helper.setCell(row, null, 1);
        helper.setCell(row, null, 2);
        helper.setCell(row, null, 3);
        helper.setCell(row, null, 5);
        helper.setCell(row, null, 6);
    }

    private void fillSummary(Estimation estimation, Map<String, String> request) {
        Row row = createRow(ROW_HEIGHT);
        mergeCells(0, 3);
        mergeCells(4, 6);

        helper.setTotalCell(row, messageBundle.getString("cellName.summary"), 0);
        helper.setMarkedCell(row, math.getEstimationMaxHours(estimation, request), 4);
        helper.setMarkedCell(row, math.getEstimationMaxCost(estimation, request), 7);

        helper.setMarkedCell(row, null, 1);
        helper.setMarkedCell(row, null, 2);
        helper.setMarkedCell(row, null, 3);
        helper.setMarkedCell(row, null, 5);
        helper.setMarkedCell(row, null, 6);
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

package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

public class PhaseEstimationSheet extends Sheet {

    public PhaseEstimationSheet(ExcelWorkbook excelWorkbook) {
        helper = excelWorkbook;
    }

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка по фазам");
        configureColumns();

        fillHeader();

        for (Phase phase : estimation.getPhases()) {
            fillPhaseRow(phase, request);
        }

        fillSummary();
    }

    private void fillHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);

        helper.setHeaderCell(row, "Фаза", 0);
        helper.setHeaderCell(row, "Часы", 1);
        helper.setHeaderCell(row, "Стоимость, RUB", 2);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, phase.getName(), 0);

        double sumHoursMax = ReportMath.calcListSummaryMaxHours(phase.getTasks(), request);
        hoursMaxSummary += sumHoursMax;
        helper.setCell(row, sumHoursMax, 1);

        double sumCostMax = ReportMath.calcListSummaryMaxCost(phase.getTasks(), request);
        costMaxSummary += sumCostMax;
        helper.setCell(row, sumCostMax, 2);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);

        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursMaxSummary, 1);
        helper.setMarkedCell(row, costMaxSummary, 2);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
    }
}

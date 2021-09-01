package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.ReportMath;

@RequiredArgsConstructor
public class PhaseEstimationSheet implements Sheet {

    private final ExcelWorkbook helper;
    private XSSFSheet sheet;
    private int rowNum = 0;
    private final short ROW_HEIGHT = 380;
    private double hoursMaxSummary;
    private double costMaxSummary;

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        sheet = helper.getWorkbook().createSheet("Оценка по фазам");

        configure();
        fillHeader();

        for (Phase phase : estimation.getPhases()) {
            Row row = createRow(ROW_HEIGHT);
            helper.setPhaseCell(row, phase.getName(), 0);

            double sumHoursMax = ReportMath.calcPhaseSummaryMaxHours(phase, request);
            hoursMaxSummary += sumHoursMax;
            helper.setPhaseCell(row, sumHoursMax, 1);

            double sumCostMax = ReportMath.calcPhaseSummaryMaxCost(phase, request);
            costMaxSummary += sumCostMax;
            helper.setPhaseCell(row, sumCostMax, 2);
        }
        fillSummary();
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        helper.setPhaseCell(row, "Итого по проекту:", 0);
        helper.setPhaseCell(row, hoursMaxSummary, 1);
        helper.setPhaseCell(row, costMaxSummary, 2);
    }

    private void fillHeader() {
        final short HEADER_ROW_HEIGHT = 1050;
        Row row = createRow(HEADER_ROW_HEIGHT);
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 1);
        helper.setHeaderCell(row, "Стоимость, RUB", 2);
    }

    private Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;
        return row;
    }

    private void configure() {
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 6000);
    }
}

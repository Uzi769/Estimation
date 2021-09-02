package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

@RequiredArgsConstructor
public class PhaseEstimationSheet implements Sheet {

    private final ExcelWorkbook helper;
    private XSSFSheet sheet;

    private double hoursSummary;
    private double costSummary;

    private final short ROW_HEIGHT = 380;

    private int rowNum = 0;

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
        helper.setHeaderCell(row, "Описание", 1);
        helper.setHeaderCell(row, "Часы", 2);
        helper.setHeaderCell(row, "Стоимость, RUB", 3);
    }

    private void fillPhaseRow(Phase phase, ReportRequest request) {
        Row row = createRow(ROW_HEIGHT);

        helper.setCell(row, phase.getName(), 0);

        double sumHoursMax = ReportMath.calcPhaseSummaryMaxHours(phase, request);
        hoursSummary += sumHoursMax;
        helper.setCell(row, sumHoursMax, 2);

        double sumCostMax = ReportMath.calcPhaseSummaryMaxCost(phase, request);
        costSummary += sumCostMax;
        helper.setCell(row, sumCostMax, 3);
    }

    private void fillSummary() {
        Row row = createRow(ROW_HEIGHT);
        mergeSummary();

        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setMarkedCell(row, hoursSummary, 2);
        helper.setMarkedCell(row, costSummary, 3);
    }

    private void configureColumns() {
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
    }

    private Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;

        return row;
    }

    private void mergeSummary() {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
    }
}

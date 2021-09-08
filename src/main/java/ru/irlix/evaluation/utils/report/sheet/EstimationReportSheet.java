package ru.irlix.evaluation.utils.report.sheet;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

@Getter
public abstract class EstimationReportSheet {
    public abstract void getSheet(Estimation estimation, ReportRequest request);

    protected ExcelWorkbook helper;
    protected HSSFSheet sheet;

    protected double hoursMinSummary;
    protected double hoursMaxSummary;
    protected double costMinSummary;
    protected double costMaxSummary;

    protected final short ROW_HEIGHT = 380;
    protected final short HEADER_ROW_HEIGHT = 1050;

    protected int rowNum = 0;

    protected Row createRow(short height) {
        Row row = sheet.createRow(rowNum);
        row.setHeight(height);
        rowNum++;

        return row;
    }

    protected void mergeCells(int startColumn, int endColumn) {
        int currentRow = rowNum - 1;
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, endColumn));
    }

    protected void mergeCells(int startRow, int endRow, int startColumn, int endColumn) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
    }

    protected boolean isFeature(Task task) {
        return EntitiesIdConstants.FEATURE_ID.equals(task.getType().getId());
    }

    protected void fillReportHeader(Estimation estimation, ReportRequest request, int lastColumn) {
        ReportHeader header = new ReportHeader(this);
        header.fillHeader(estimation, request, lastColumn);
    }
}

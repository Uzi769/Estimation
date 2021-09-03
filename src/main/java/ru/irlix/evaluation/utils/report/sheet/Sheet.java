package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

public abstract class Sheet {
    public abstract void getSheet(Estimation estimation, ReportRequest request);

    protected ExcelWorkbook helper;
    protected XSSFSheet sheet;

    protected double hoursMinSummary;
    protected double hoursMaxSummary;
    protected double costMinSummary;
    protected double costMaxSummary;

    protected final short ROW_HEIGHT = 380;

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

    protected boolean isFeature(Task task) {
        return EntityConstants.FEATURE_ID.equals(task.getType().getId());
    }
}

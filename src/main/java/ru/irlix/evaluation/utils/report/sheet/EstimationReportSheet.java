package ru.irlix.evaluation.utils.report.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import ru.irlix.evaluation.config.UTF8Control;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.utils.constant.LocaleConstants;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.Map;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Getter
public abstract class EstimationReportSheet {

    protected final EstimationMath math;

    protected final ExcelWorkbook helper;

    public abstract void getSheet(Estimation estimation, Map<String, String> request);

    protected final ResourceBundle messageBundle = ResourceBundle.getBundle(
            "messages",
            LocaleConstants.DEFAULT_LOCALE,
            new UTF8Control()
    );

    protected HSSFSheet sheet;

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

    protected void fillReportHeader(Estimation estimation, Map<String, String> request, int lastColumn) {
        ReportHeader header = new ReportHeader(this);
        header.fillHeader(estimation, request, lastColumn);
    }
}

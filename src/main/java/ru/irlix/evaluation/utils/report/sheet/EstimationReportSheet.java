package ru.irlix.evaluation.utils.report.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import ru.irlix.evaluation.config.UTF8Control;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.constant.LocaleConstants;
import ru.irlix.evaluation.utils.constant.ReportConstants;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<String> getRoleCosts(Estimation estimation, Map<String, String> request) {
        Set<Role> roles = math.getRolesMap(estimation).keySet();

        List<String> rolesStrings = roles.stream()
                .map(r -> r.getValue() + ReportConstants.COST)
                .collect(Collectors.toList());

        List<Task> allTasks = math.getRolesMap(estimation).values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (math.getQaSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add(ReportConstants.QA_COST);
        }

        if (math.getPmSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add(ReportConstants.PM_COST);
        }

        return rolesStrings;
    }

    protected void fillReportHeader(Estimation estimation, Map<String, String> request, int lastColumn) {
        ReportHeader header = new ReportHeader(this);
        header.fillHeader(estimation, request, lastColumn);
    }
}

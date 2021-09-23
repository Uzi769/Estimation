package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.localization.MessageBundle;
import ru.irlix.evaluation.utils.math.EstimationMath;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Log4j2
public class ReportHeader {

    private final EstimationReportSheet sheet;
    private int additionalRowsCount = 0;

    private final int DEFAULT_ROWS_COUNT = 16;

    private final ResourceBundle messageBundle = MessageBundle.getMessageBundle();

    public void fillHeader(Estimation estimation, Map<String, String> request, int lastColumn) {
        int ENDING_ROWS_COUNT = 7;

        IntStream.range(0, DEFAULT_ROWS_COUNT).forEach(i -> sheet.createRow(sheet.ROW_HEIGHT));

        setImage(lastColumn);
        setInfo(estimation, lastColumn);
        fillRoleTable(estimation, request);
        fillPhaseTable(estimation);

        int descriptionFirstRow = DEFAULT_ROWS_COUNT + additionalRowsCount + 1;

        IntStream.range(0, ENDING_ROWS_COUNT).forEach(i -> sheet.createRow(sheet.ROW_HEIGHT));

        setDescription(lastColumn, descriptionFirstRow);
        sheet.getHelper().setNonBorderCell(sheet.getSheet().getRow(descriptionFirstRow + 4),
                messageBundle.getString("string.approximateEstimation"), 0);

        sheet.mergeCells(0, 6, 0, lastColumn);
        sheet.mergeCells(14, 14, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow - 1, descriptionFirstRow - 1, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 3, descriptionFirstRow + 3, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 4, descriptionFirstRow + 4, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 5, descriptionFirstRow + 5, 0, lastColumn);

        if (descriptionFirstRow != 17) {
            sheet.mergeCells(15, descriptionFirstRow - 2, 3, 3);
        }

        if (descriptionFirstRow != 17 && lastColumn != 7) {
            sheet.mergeCells(15, descriptionFirstRow - 2, 7, lastColumn);
        }
    }

    private void setImage(int lastColumn) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/static/irlixLogo.png");
            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = sheet.getHelper().getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            inputStream.close();
            Drawing<HSSFShape> drawing = sheet.getSheet().createDrawingPatriarch();
            ClientAnchor anchor = sheet.getHelper().getCreateHelper().createClientAnchor();

            anchor.setCol1(lastColumn);
            anchor.setRow1(1);
            anchor.setCol2(lastColumn + 1);
            anchor.setRow2(6);

            drawing.createPicture(anchor, pictureIdx);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setInfo(Estimation estimation, int lastColumn) {
        Row row = sheet.getSheet().getRow(7);
        String client = (estimation.getClient() == null) ? messageBundle.getString("string.client") : estimation.getClient();
        sheet.getHelper().setNonBorderHeaderCell(row, messageBundle.getString("string.commercialOffer") +
                " «" + client + "»", 0);
        sheet.mergeCells(7, 8, 0, lastColumn);

        row = sheet.getSheet().getRow(9);
        sheet.getHelper().setNonBorderMarkedCell(row, messageBundle.getString("cellName.project"), 0);
        sheet.getHelper().setNonBorderCell(row, estimation.getName(), 3);
        sheet.mergeCells(9, 9, 0, 2);
        sheet.mergeCells(9, 9, 3, lastColumn);

        row = sheet.getSheet().getRow(10);
        sheet.getHelper().setNonBorderMarkedCell(row, messageBundle.getString("cellName.contacts"), 0);
        sheet.getHelper().setNonBorderCell(row, messageBundle.getString("string.contacts"), 3);
        sheet.mergeCells(10, 10, 0, 2);
        sheet.mergeCells(10, 10, 3, lastColumn);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        row = sheet.getSheet().getRow(11);
        sheet.getHelper().setNonBorderMarkedCell(row, messageBundle.getString("cellName.date"), 0);
        sheet.getHelper().setNonBorderCell(row, formatter.format(new Date()), 3);
        sheet.mergeCells(11, 11, 0, 2);
        sheet.mergeCells(11, 11, 3, lastColumn);

        row = sheet.getSheet().getRow(12);
        sheet.getHelper().setNonBorderMarkedCell(row, messageBundle.getString("cellName.description"), 0);
        sheet.mergeCells(12, 12, 0, 2);
        sheet.mergeCells(12, 12, 3, lastColumn);

        row = sheet.getSheet().getRow(13);
        sheet.getHelper().setNonBorderMarkedCell(row, messageBundle.getString("cellName.projectRestrictions"), 0);
        sheet.getHelper().setNonBorderCell(row, messageBundle.getString("string.projectRestrictions"), 3);
        sheet.mergeCells(13, 13, 0, 2);
        sheet.mergeCells(13, 13, 3, lastColumn);
    }

    private void setDescription(int lastColumn, int startRow) {
        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(startRow),
                messageBundle.getString("cellName.approach"), 0);
        sheet.getHelper().setBigTextCell(sheet.getSheet().getRow(startRow + 1),
                messageBundle.getString("string.approach"), 0);
        sheet.mergeCells(startRow, startRow, 0, lastColumn);
        sheet.mergeCells(startRow + 1, startRow + 2, 0, lastColumn);
    }

    private void fillRoleTable(Estimation estimation, Map<String, String> request) {
        List<String> roles = getRoles(estimation, request);

        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15),
                messageBundle.getString("columnName.employer"), 4);
        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15),
                messageBundle.getString("columnName.count"), 6);
        sheet.mergeCells(15, 15, 4, 5);

        int rowCount = 0;
        for (String role : roles) {
            if (rowCount + 1 > additionalRowsCount) {
                sheet.createRow(sheet.ROW_HEIGHT);
                additionalRowsCount++;
            }

            int rowNum = rowCount + DEFAULT_ROWS_COUNT;
            Row row = sheet.getSheet().getRow(rowNum);
            sheet.mergeCells(rowNum, rowNum, 4, 5);

            sheet.getHelper().setNonBorderCell(row, role, 4);
            sheet.getHelper().setNonBorderDigitCell(row, 1, 6);
            rowCount++;
        }

        mergeOtherColumn(estimation.getPhases().size(), roles.size());
    }

    private void fillPhaseTable(Estimation estimation) {
        List<String> phases = estimation.getPhases().stream()
                .map(Phase::getName)
                .collect(Collectors.toList());

        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15),
                messageBundle.getString("columnName.direction"), 0);
        sheet.mergeCells(15, 15, 0, 2);

        int rowCount = 0;
        for (String phase : phases) {
            if (rowCount + 1 > additionalRowsCount) {
                sheet.createRow(sheet.ROW_HEIGHT);
                additionalRowsCount++;
            }

            int rowNum = rowCount + DEFAULT_ROWS_COUNT;
            sheet.mergeCells(rowNum, rowNum, 0, 2);
            sheet.getHelper().setNonBorderCell(sheet.getSheet().getRow(rowNum), phase, 0);
            rowCount++;
        }
    }

    private List<String> getRoles(Estimation estimation, Map<String, String> request) {
        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases()
                .forEach(p -> p.getTasks()
                        .forEach(t -> {
                            if (EstimationReportSheet.isFeature(t)) {
                                allTasks.addAll(t.getTasks());
                            } else {
                                allTasks.add(t);
                            }
                        }));

        Set<Role> roles = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getRole))
                .keySet();

        List<String> rolesStrings = roles.stream()
                .map(Role::getDisplayValue)
                .collect(Collectors.toList());

        if (EstimationMath.calcQaSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add(messageBundle.getString("cellName.tester"));
        }

        if (EstimationMath.calcPmSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add(messageBundle.getString("cellName.projectManager"));
        }

        return rolesStrings;
    }

    private void mergeOtherColumn(int phaseCount, int roleCount) {
        int startRow = Math.min(phaseCount, roleCount) + DEFAULT_ROWS_COUNT;
        int endRow = Math.max(phaseCount, roleCount) + DEFAULT_ROWS_COUNT;

        if (phaseCount < roleCount) {
            while (startRow < endRow) {
                sheet.mergeCells(startRow, startRow, 0, 2);
                startRow++;
            }
        } else if (phaseCount > roleCount) {
            while (startRow < endRow) {
                sheet.mergeCells(startRow, startRow, 4, 5);
                startRow++;
            }
        }
    }
}

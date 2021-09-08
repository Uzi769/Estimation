package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
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
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ReportHeader {
    
    private final EstimationReportSheet sheet;
    private int additionalRowsCount = 0;

    private final int DEFAULT_ROWS_COUNT = 21;

    public void fillHeader(Estimation estimation, ReportRequest request, int lastColumn) {
        int ENDING_ROWS_COUNT = 6;

        IntStream.range(0, DEFAULT_ROWS_COUNT).forEach(i -> sheet.createRow(sheet.ROW_HEIGHT));

        setImage(lastColumn);
        setInfo(estimation, lastColumn);
        fillRoleTable(estimation, request);
        fillPhaseTable(estimation);

        int descriptionFirstRow = DEFAULT_ROWS_COUNT + additionalRowsCount;

        IntStream.range(0, ENDING_ROWS_COUNT).forEach(i -> sheet.createRow(sheet.ROW_HEIGHT));

        setDescription(lastColumn, descriptionFirstRow);
        sheet.getHelper().setNonBorderCell(sheet.getSheet().getRow(descriptionFirstRow + 4),
                "Ниже указана ориентировочная оценка проекта", 1);
        
        sheet.mergeCells(0, 6, 0, lastColumn);
        sheet.mergeCells(14, 14, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow - 1, descriptionFirstRow - 1, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 3, descriptionFirstRow + 3, 0, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 4, descriptionFirstRow + 4, 1, lastColumn);
        sheet.mergeCells(descriptionFirstRow + 5, descriptionFirstRow + 5, 0, lastColumn);

        sheet.mergeCells(7, 13, 0, 0);
        sheet.mergeCells(15, descriptionFirstRow - 2, 0, 0);
        sheet.mergeCells(descriptionFirstRow, descriptionFirstRow + 2, 0, 0);
        sheet.mergeCells(15, descriptionFirstRow - 2, 3, 3);
        sheet.mergeCells(15, descriptionFirstRow - 2, 7, lastColumn);
    }

    private void setImage(int lastColumn) {
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/static/logo.png");
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
            e.printStackTrace();
        }
    }

    private void setInfo(Estimation estimation, int lastColumn) {
        Row row = sheet.getSheet().getRow(7);
        sheet.getHelper().setNonBorderHeaderCell(row, "Коммерческое предложение для компании ООО «" + estimation.getClient() + "»", 1);
        sheet.mergeCells(7, 8, 1, lastColumn);

        row = sheet.getSheet().getRow(9);
        sheet.getHelper().setNonBorderMarkedCell(row, "Проект", 1);
        sheet.getHelper().setNonBorderCell(row, estimation.getName(), 3);
        sheet.mergeCells(9, 9, 1, 2);
        sheet.mergeCells(9, 9, 3, lastColumn);

        row = sheet.getSheet().getRow(10);
        sheet.getHelper().setNonBorderMarkedCell(row, "Контакты", 1);
        sheet.getHelper().setNonBorderCell(row, "Валерий Руссков, +7 902-125-06-54, valery.russkov@irlix.ru", 3);
        sheet.mergeCells(10, 10, 1, 2);
        sheet.mergeCells(10, 10, 3, lastColumn);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        row = sheet.getSheet().getRow(11);
        sheet.getHelper().setNonBorderMarkedCell(row, "Дата", 1);
        sheet.getHelper().setNonBorderCell(row, formatter.format(new Date()), 3);
        sheet.mergeCells(11, 11, 1, 2);
        sheet.mergeCells(11, 11, 3, lastColumn);

        row = sheet.getSheet().getRow(12);
        sheet.getHelper().setNonBorderMarkedCell(row, "Описание", 1);
        sheet.mergeCells(12, 12, 1, 2);
        sheet.mergeCells(12, 12, 3, lastColumn);

        row = sheet.getSheet().getRow(13);
        sheet.getHelper().setNonBorderMarkedCell(row, "Ограничения проекта", 1);
        sheet.getHelper().setNonBorderCell(row, "Оценка осуществлена на основе технического задания " +
                "предоставленного заказчиком и 1 конф-колла.", 3);
        sheet.mergeCells(13, 13, 1, 2);
        sheet.mergeCells(13, 13, 3, lastColumn);
    }

    private void setDescription(int lastColumn, int startRow) {
        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(startRow), "Подход к реализации продуктовых проектов", 1);
        sheet.getHelper().setBigTextCell(sheet.getSheet().getRow(startRow + 1), "Компания IRLIX применяет собственный подход в реализации " +
                "проектов. Agile совместно с Scrum Framework. В качестве основных атрибутов задействованы основные " +
                "практики Scrum, образующие внутренний регламент разработки продуктовых проектов в компании IRLIX.", 1);
        sheet.mergeCells(startRow, startRow, 1, lastColumn);
        sheet.mergeCells(startRow + 1, startRow + 2, 1, lastColumn);
    }

    private void fillRoleTable(Estimation estimation, ReportRequest request) {
        List<String> roles = getRoles(estimation, request);

        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15), "Сотрудник", 4);
        sheet.mergeCells(15, 15, 4, 5);
        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15), "Кол-во", 6);

        int currentRow = 16;
        for (String role : roles) {
            sheet.mergeCells(currentRow, currentRow, 4, 5);
            Row row = sheet.getSheet().getRow(currentRow);
            sheet.getHelper().setNonBorderCell(row, role, 4);
            sheet.getHelper().setNonBorderDigitCell(row, 1, 6);
            currentRow++;

            if (currentRow >= DEFAULT_ROWS_COUNT + additionalRowsCount) {
                sheet.createRow(sheet.ROW_HEIGHT);
                additionalRowsCount++;
            }
        }

        mergeOtherColumn(estimation.getPhases().size(), roles.size());
    }

    private void fillPhaseTable(Estimation estimation) {
        List<String> phases = estimation.getPhases().stream()
                .map(Phase::getName)
                .collect(Collectors.toList());

        sheet.getHelper().setNonBorderMarkedCell(sheet.getSheet().getRow(15), "Направления", 1);
        sheet.mergeCells(15, 15, 1, 2);

        int currentRow = 16;
        for (String phase : phases) {
            sheet.mergeCells(currentRow, currentRow, 1, 2);
            sheet.getHelper().setNonBorderCell(sheet.getSheet().getRow(currentRow), phase, 1);
            currentRow++;

            if (currentRow >= DEFAULT_ROWS_COUNT + additionalRowsCount) {
                sheet.createRow(sheet.ROW_HEIGHT);
                additionalRowsCount++;
            }
        }
    }

    private List<String> getRoles(Estimation estimation, ReportRequest request) {
        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases()
                .forEach(p -> p.getTasks()
                        .forEach(t -> {
                            if (sheet.isFeature(t)) {
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

        if (ReportMath.calcQaSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add("Специалист по тестированию");
        }

        if (ReportMath.calcPmSummaryMaxHours(allTasks, request) > 0) {
            rolesStrings.add("Руководитель проекта");
        }

        return rolesStrings;
    }

    private void mergeOtherColumn(int phaseCount, int roleCount) {
        int startRow = Math.min(phaseCount, roleCount) + 16;
        int endRow = Math.max(phaseCount, roleCount) + 16;

        if (phaseCount < roleCount) {
            while (startRow < endRow) {
                sheet.mergeCells(startRow, startRow, 1, 2);
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

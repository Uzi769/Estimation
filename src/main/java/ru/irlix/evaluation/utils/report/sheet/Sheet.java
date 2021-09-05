package ru.irlix.evaluation.utils.report.sheet;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;
import ru.irlix.evaluation.utils.report.math.ReportMath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Sheet {
    public abstract void getSheet(Estimation estimation, ReportRequest request);

    protected ExcelWorkbook helper;
    protected HSSFSheet sheet;

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

    protected void mergeCells(int startRow, int endRow, int startColumn, int endColumn) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
    }

    protected void setImage() {
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/static/logo.png");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = helper.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            inputStream.close();
            Drawing<HSSFShape> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.getCreateHelper().createClientAnchor();

            anchor.setCol1(1);
            anchor.setRow1(1);
            anchor.setCol2(3);
            anchor.setRow2(7);

            mergeCells(1, 6, 1, 2);

            drawing.createPicture(anchor, pictureIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setInfo(Estimation estimation) {
        Row row = sheet.getRow(2);
        helper.setBoldCell(row, "Заголовок", 4);
        helper.setCell(row, "Коммерческое предложение для компании ООО «" + estimation.getClient() + "»", 5);
        addBorderToMergeRegion(row);
        mergeCells(2, 2, 5, 8);

        row = sheet.getRow(3);
        helper.setBoldCell(row, "Проект", 4);
        helper.setCell(row, estimation.getName(), 5);
        addBorderToMergeRegion(row);
        mergeCells(3, 3, 5, 8);

        row = sheet.getRow(4);
        helper.setBoldCell(row, "Контакты", 4);
        helper.setCell(row, "Валерий Руссков, +7 902-125-06-54, valery.russkov@irlix.ru", 5);
        addBorderToMergeRegion(row);
        mergeCells(4, 4, 5, 8);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        row = sheet.getRow(5);
        helper.setBoldCell(row, "Дата", 4);
        helper.setCell(row, formatter.format(new Date()), 5);
        addBorderToMergeRegion(row);
        mergeCells(5, 5, 5, 8);
    }

    protected void setDescription() {
        helper.setBoldCell(sheet.getRow(8), "Ограничения проекта", 1);
        helper.setBigTextCell(sheet.getRow(9), "Оценка осуществлена на основе технического задания " +
                "предоставленного заказчиком и 1 конф-колла.", 1);
        mergeCells(9, 11, 1, 2);
        helper.setCell(sheet.getRow(8), null, 2);
        helper.setCell(sheet.getRow(9), null, 2);
        helper.setCell(sheet.getRow(10), null, 1);
        helper.setCell(sheet.getRow(10), null, 2);
        helper.setCell(sheet.getRow(11), null, 1);
        helper.setCell(sheet.getRow(11), null, 2);

        helper.setBoldCell(sheet.getRow(13), "Подход к реализации продуктовых проектов", 1);
        helper.setBigTextCell(sheet.getRow(14), "Компания IRLIX применяет собственный подход в реализации " +
                "проектов. Agile совместно с Scrum Framework. В качестве основных атрибутов задействованы основные " +
                "практики Scrum, образующие внутренний регламент разработки продуктовых проектов в компании IRLIX.", 1);
        mergeCells(14, 19, 1, 2);
        helper.setCell(sheet.getRow(13), null, 2);
        helper.setCell(sheet.getRow(14), null, 2);

        for (int i = 15; i < 20; i++) {
            helper.setCell(sheet.getRow(i), null, 1);
            helper.setCell(sheet.getRow(i), null, 2);
        }
    }

    protected void fillRoleTable(Estimation estimation, ReportRequest request) {
        List<String> roles = getRoles(estimation, request);

        helper.setBoldCell(sheet.getRow(8), "Сотрудник", 4);
        helper.setCell(sheet.getRow(8), null, 5);
        mergeCells(8, 8, 4, 5);
        helper.setBoldCell(sheet.getRow(8), "Кол-во", 6);

        for (int i = 9; i < 20; i++) {
            Row row = sheet.getRow(i);

            helper.setCell(row, null, 5);
            mergeCells(i, i, 4, 5);

            if (i - 9 < roles.size()) {
                helper.setCell(row, roles.get(i - 9), 4);
                helper.setCell(row, 1, 6);
            } else {
                helper.setCell(row, null, 4);
                helper.setCell(row, null, 6);
            }

        }
    }

    protected void fillPhaseTable(Estimation estimation) {
        List<String> phases = estimation.getPhases().stream()
                .map(Phase::getName)
                .collect(Collectors.toList());

        helper.setBoldCell(sheet.getRow(8), "Направления", 8);

        for (int i = 9; i < 20; i++) {
            Row row = sheet.getRow(i);

            if (i - 9 < phases.size()) {
                helper.setCell(row, phases.get(i - 9), 8);
            } else {
                helper.setCell(row, null, 8);
            }
        }
    }

    private List<String> getRoles(Estimation estimation, ReportRequest request) {
        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases()
                .forEach(p -> p.getTasks()
                        .forEach(t -> {
                            if (isFeature(t)) {
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

    private void addBorderToMergeRegion(Row row) {
        helper.setCell(row, null, 6);
        helper.setCell(row, null, 7);
        helper.setCell(row, null, 8);
    }

    protected boolean isFeature(Task task) {
        return EntityConstants.FEATURE_ID.equals(task.getType().getId());
    }

    protected void fillReportHeader(Estimation estimation, ReportRequest request) {
        IntStream.range(0, 25).forEach(i -> createRow(ROW_HEIGHT));
        setImage();
        setInfo(estimation);
        setDescription();
        fillRoleTable(estimation, request);
        fillPhaseTable(estimation);

        helper.setNonBorderCell(sheet.getRow(21), "Ниже указана ориентировочная оценка проекта", 1);
        mergeCells(21, 21, 1, 8);
    }
}

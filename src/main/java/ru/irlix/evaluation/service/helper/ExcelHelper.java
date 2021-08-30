package ru.irlix.evaluation.service.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class ExcelHelper {

    private XSSFWorkbook workbook;
    private CreationHelper createHelper;
    private Font headerFont;
    private Font defaultFont;
    private CellStyle headerCellStyle;
    private CellStyle stringCellStyle;
    private CellStyle digitCellStyle;
    private CellStyle dateCellStyle;

    public Resource getEstimationReportResource(List<Estimation> estimations) throws IOException {
        workbook = new XSSFWorkbook();
        createHelper = workbook.getCreationHelper();
        XSSFSheet sheet = workbook.createSheet("Estimations");
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 10000);
        sheet.setColumnWidth(5, 10000);
        sheet.setColumnWidth(6, 10000);

        int rowNum = 0;

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 600);
        setHeaderCell(row, "Id", 0);
        setHeaderCell(row, "Name", 1);
        setHeaderCell(row, "Date of creation", 2);
        setHeaderCell(row, "Risk", 3);
        setHeaderCell(row, "Status", 4);
        setHeaderCell(row, "Client", 5);
        setHeaderCell(row, "Creator", 6);

        for (Estimation estimation : estimations) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeight((short) 350);

            setCell(row, estimation.getId(), 0);
            setCell(row, estimation.getName(), 1);
            setCell(row, estimation.getCreateDate(), 2);

            if (estimation.getRisk() != null) {
                setCell(row, estimation.getRisk(), 3);
            }

            setCell(row, estimation.getStatus().getDisplayValue(), 4);
            setCell(row, estimation.getClient(), 5);
            setCell(row, estimation.getCreator(), 6);
        }

        File file = new File("C:/output/estimations.xlsx");

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        workbook.close();

        return new FileSystemResource("C:/output/estimations.xlsx");
    }

    private void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getStringCellStyle());
    }

    private void setCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(digit);
        cell.setCellStyle(getDigitCellStyle());
    }

    private void setCell(Row row, Instant date, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(Date.from(date));
        cell.setCellStyle(getDateCellStyle());
    }

    private void setHeaderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getHeaderStyle());
    }

    private Font getHeaderFont() {
        if (headerFont == null) {
            headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setFontName("Trebuchet MS");
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerFont.setItalic(false);
        }

        return headerFont;
    }

    private Font getDefaultFont() {
        if (defaultFont == null) {
            defaultFont = workbook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Trebuchet MS");
            defaultFont.setColor(IndexedColors.BLACK.getIndex());
            defaultFont.setBold(false);
            defaultFont.setItalic(false);
        }

        return defaultFont;
    }

    private CellStyle getHeaderStyle() {
        if (headerCellStyle == null) {
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setFont(getHeaderFont());
        }

        return headerCellStyle;
    }

    private CellStyle getDateCellStyle() {
        if (dateCellStyle == null) {
            dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy hh:mm"));
            dateCellStyle.setFont(getDefaultFont());
        }

        return dateCellStyle;
    }

    private CellStyle getDigitCellStyle() {
        if (digitCellStyle == null) {
            digitCellStyle = workbook.createCellStyle();
            digitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            digitCellStyle.setFont(getDefaultFont());
        }

        return digitCellStyle;
    }

    private CellStyle getStringCellStyle() {
        if (stringCellStyle == null) {
            stringCellStyle = workbook.createCellStyle();
            stringCellStyle.setFont(getDefaultFont());
        }

        return stringCellStyle;
    }
}

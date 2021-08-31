package ru.irlix.evaluation.utils.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class ExcelWorkbook {

    private XSSFWorkbook workbook;
    private CreationHelper createHelper;
    private Font headerFont;
    private Font defaultFont;
    private CellStyle headerCellStyle;
    private CellStyle stringCellStyle;
    private CellStyle digitCellStyle;
    private CellStyle dateCellStyle;

    public ExcelWorkbook() {
        workbook = new XSSFWorkbook();
        createHelper = workbook.getCreationHelper();
    }

    public void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getStringCellStyle());
    }

    public void setCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(digit);
        cell.setCellStyle(getDigitCellStyle());
    }

    public void setCell(Row row, Instant date, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(Date.from(date));
        cell.setCellStyle(getDateCellStyle());
    }

    public void setHeaderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getHeaderStyle());
    }

    public Font getHeaderFont() {
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

    public Font getDefaultFont() {
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

    public CellStyle getHeaderStyle() {
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

    public CellStyle getDateCellStyle() {
        if (dateCellStyle == null) {
            dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy hh:mm"));
            dateCellStyle.setFont(getDefaultFont());
        }

        return dateCellStyle;
    }

    public CellStyle getDigitCellStyle() {
        if (digitCellStyle == null) {
            digitCellStyle = workbook.createCellStyle();
            digitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            digitCellStyle.setFont(getDefaultFont());
        }

        return digitCellStyle;
    }

    public CellStyle getStringCellStyle() {
        if (stringCellStyle == null) {
            stringCellStyle = workbook.createCellStyle();
            stringCellStyle.setFont(getDefaultFont());
        }

        return stringCellStyle;
    }
}

package ru.irlix.evaluation.utils.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Getter
@Setter
public class ExcelWorkbook {

    private XSSFWorkbook workbook;
    private CreationHelper createHelper;
    private Font headerFont;
    private Font defaultFont;
    private Font boldFont;

    private CellStyle headerCellStyle;
    private CellStyle phaseCellStyle;
    private CellStyle phaseDigitCellStyle;
    private CellStyle boldCellStyle;

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

    public void setHeaderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getHeaderCellStyle());
    }

    public void setPhaseCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getPhaseCellStyle());
    }

    public void setPhaseCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(digit);
        cell.setCellStyle(getPhaseDigitCellStyle());
    }

    public void setBoldCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getBoldCellStyle());
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

    public Font getBoldFont() {
        if (boldFont == null) {
            boldFont = workbook.createFont();
            boldFont.setFontHeightInPoints((short) 11);
            boldFont.setFontName("Trebuchet MS");
            boldFont.setColor(IndexedColors.BLACK.getIndex());
            boldFont.setBold(true);
            boldFont.setItalic(false);
        }

        return boldFont;
    }

    public CellStyle getHeaderCellStyle() {
        if (headerCellStyle == null) {
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setFont(getHeaderFont());
            headerCellStyle.setWrapText(true);
        }

        return headerCellStyle;
    }

    public CellStyle getPhaseCellStyle() {
        if (phaseCellStyle == null) {
            phaseCellStyle = workbook.createCellStyle();
            phaseCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            phaseCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            phaseCellStyle.setFont(getDefaultFont());
            phaseCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return phaseCellStyle;
    }

    public CellStyle getPhaseDigitCellStyle() {
        if (phaseDigitCellStyle == null) {
            phaseDigitCellStyle = workbook.createCellStyle();
            phaseDigitCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            phaseDigitCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            phaseDigitCellStyle.setFont(getDefaultFont());
            phaseDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }

        return phaseDigitCellStyle;
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

    public CellStyle getBoldCellStyle() {
        if (boldCellStyle == null) {
            boldCellStyle = workbook.createCellStyle();
            boldCellStyle.setFont(getBoldFont());
        }

        return boldCellStyle;
    }
}

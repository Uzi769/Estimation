package ru.irlix.evaluation.utils.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
    private  DecimalFormat formatter;


    public ExcelWorkbook() {
        workbook = new XSSFWorkbook();
        createHelper = workbook.getCreationHelper();
        formatter = new DecimalFormat("#.#");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getStringCellStyle());
    }

    public void setCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(formatter.format(digit));
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
        cell.setCellValue(formatter.format(digit));
        cell.setCellStyle(getPhaseDigitCellStyle());
    }

    public void setBoldCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getBoldCellStyle());
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

    private Font getBoldFont() {
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

    private CellStyle getHeaderCellStyle() {
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

    private CellStyle getPhaseCellStyle() {
        if (phaseCellStyle == null) {
            phaseCellStyle = workbook.createCellStyle();
            phaseCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            phaseCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            phaseCellStyle.setFont(getDefaultFont());
            phaseCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return phaseCellStyle;
    }

    private CellStyle getPhaseDigitCellStyle() {
        if (phaseDigitCellStyle == null) {
            phaseDigitCellStyle = workbook.createCellStyle();
            phaseDigitCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            phaseDigitCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            phaseDigitCellStyle.setFont(getDefaultFont());
            phaseDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }

        return phaseDigitCellStyle;
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

    private CellStyle getBoldCellStyle() {
        if (boldCellStyle == null) {
            boldCellStyle = workbook.createCellStyle();
            boldCellStyle.setFont(getBoldFont());
        }

        return boldCellStyle;
    }

    public Resource save(String path) throws IOException {
        File file = new File(path);

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        workbook.close();

        return new FileSystemResource(path);
    }
}

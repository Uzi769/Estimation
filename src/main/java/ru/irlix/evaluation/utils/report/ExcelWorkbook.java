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
    private CellStyle markedCellStyle;
    private CellStyle markedDigitCellStyle;
    private CellStyle boldCellStyle;
    private CellStyle totalCellStyle;

    private CellStyle stringCellStyle;
    private CellStyle digitCellStyle;
    private CellStyle dateCellStyle;
    private DecimalFormat formatter;


    public ExcelWorkbook() {
        workbook = new XSSFWorkbook();
        createHelper = workbook.getCreationHelper();
        formatter = new DecimalFormat("#.#");
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

    public void setMarkedCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getMarkedCellStyle());
    }

    public void setMarkedCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(formatter.format(digit));
        cell.setCellStyle(getMarkedDigitCellStyle());
    }

    public void setBoldCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getBoldCellStyle());
    }


    public void setTotalCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getTotalCellStyle());
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

    private CellStyle getMarkedCellStyle() {
        if (markedCellStyle == null) {
            markedCellStyle = workbook.createCellStyle();
            markedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            markedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            markedCellStyle.setFont(getDefaultFont());
            markedCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return markedCellStyle;
    }

    private CellStyle getMarkedDigitCellStyle() {
        if (markedDigitCellStyle == null) {
            markedDigitCellStyle = workbook.createCellStyle();
            markedDigitCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            markedDigitCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            markedDigitCellStyle.setFont(getDefaultFont());
            markedDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }

        return markedDigitCellStyle;
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

    public CellStyle getTotalCellStyle() {
        if (totalCellStyle == null) {
            totalCellStyle = workbook.createCellStyle();
            totalCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            totalCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            totalCellStyle.setFont(getDefaultFont());
            totalCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        }

        return totalCellStyle;
    }

}

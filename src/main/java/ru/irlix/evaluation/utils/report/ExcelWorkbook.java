package ru.irlix.evaluation.utils.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

@Getter
@Setter
public class ExcelWorkbook {

    private HSSFWorkbook workbook;
    private CreationHelper createHelper;

    private Font headerFont;
    private Font defaultFont;
    private Font boldFont;
    private Font mianHeaderFont;
    private Font lightFont;

    private CellStyle headerCellStyle;
    private CellStyle markedCellStyle;
    private CellStyle markedDigitCellStyle;
    private CellStyle boldCellStyle;
    private CellStyle boldDigitCellStyle;
    private CellStyle lightCellStyle;
    private CellStyle lightDigitCellStyle;
    private CellStyle totalCellStyle;
    private CellStyle lightTotalCellStyle;
    private CellStyle lightHeaderCellStyle;
    private CellStyle bigTextCellStyle;

    private CellStyle nonBorderCellStyle;
    private CellStyle nonBorderDigitCellStyle;
    private CellStyle nonBorderMarkedCellStyle;
    private CellStyle nonBorderHeaderCellStyle;

    private CellStyle stringCellStyle;
    private CellStyle digitCellStyle;
    private DecimalFormat formatter;

    public ExcelWorkbook() {
        workbook = new HSSFWorkbook();
        createHelper = workbook.getCreationHelper();
        formatter = new DecimalFormat("#.#");
    }

    public void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getStringCellStyle());
    }

    public void setBigTextCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getBigTextCellStyle());
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

    public void setLightCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getLightCellStyle());
    }

    public void setLightCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column);
        cell.setCellValue(formatter.format(digit));
        cell.setCellStyle(getLightDigitCellStyle());
    }

    public void setBoldCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getBoldCellStyle());
    }

    public void setBoldCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(digit);
        cell.setCellStyle(getBoldDigitCellStyle());
    }

    public void setNonBorderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getNonBorderCellStyle());
    }

    public void setNonBorderDigitCell(Row row, double digit, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(digit);
        cell.setCellStyle(getNonBorderDigitCellStyle());
    }

    public void setNonBorderMarkedCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getNonBorderMarkedCellStyle());
    }

    public void setNonBorderHeaderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getNonBorderHeaderCellStyle());
    }

    public void setTotalCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getTotalCellStyle());
    }

    public void setLightTotalCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getLightTotalCellStyle());
    }

    public void setLightHeaderCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
        cell.setCellStyle(getLightHeaderCellStyle());
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

    public Font getMainHeaderFont() {
        if (mianHeaderFont == null) {
            mianHeaderFont = workbook.createFont();
            mianHeaderFont.setFontHeightInPoints((short) 14);
            mianHeaderFont.setFontName("Trebuchet MS");
            mianHeaderFont.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
            mianHeaderFont.setBold(true);
            mianHeaderFont.setItalic(false);
        }

        return mianHeaderFont;
    }

    private Font getDefaultFont() {
        if (defaultFont == null) {
            defaultFont = workbook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Trebuchet MS");
            defaultFont.setColor(IndexedColors.GREY_80_PERCENT.index);
            defaultFont.setBold(false);
            defaultFont.setItalic(false);
        }

        return defaultFont;
    }

    private Font getBoldFont() {
        if (boldFont == null) {
            boldFont = workbook.createFont();
            boldFont.setFontHeightInPoints((short) 12);
            boldFont.setFontName("Trebuchet MS");
            boldFont.setColor(IndexedColors.GREY_80_PERCENT.index);
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
            setBorders(headerCellStyle);
        }

        return headerCellStyle;
    }

    private CellStyle getMarkedCellStyle() {
        if (markedCellStyle == null) {
            markedCellStyle = workbook.createCellStyle();
            markedCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            markedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            markedCellStyle.setFont(getHeaderFont());
            markedCellStyle.setAlignment(HorizontalAlignment.LEFT);
            setBorders(markedCellStyle);
        }

        return markedCellStyle;
    }

    private CellStyle getMarkedDigitCellStyle() {
        if (markedDigitCellStyle == null) {
            markedDigitCellStyle = workbook.createCellStyle();
            markedDigitCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            markedDigitCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            markedDigitCellStyle.setFont(getHeaderFont());
            markedDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            setBorders(markedDigitCellStyle);
        }

        return markedDigitCellStyle;
    }

    private CellStyle getDigitCellStyle() {
        if (digitCellStyle == null) {
            digitCellStyle = workbook.createCellStyle();
            digitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            digitCellStyle.setFont(getDefaultFont());
            setBorders(digitCellStyle);
        }

        return digitCellStyle;
    }

    private CellStyle getStringCellStyle() {
        if (stringCellStyle == null) {
            stringCellStyle = workbook.createCellStyle();
            stringCellStyle.setFont(getDefaultFont());
            setBorders(stringCellStyle);
        }

        return stringCellStyle;
    }

    private CellStyle getBigTextCellStyle() {
        if (bigTextCellStyle == null) {
            bigTextCellStyle = workbook.createCellStyle();
            bigTextCellStyle.setFont(getDefaultFont());
            bigTextCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            bigTextCellStyle.setWrapText(true);
        }

        return bigTextCellStyle;
    }

    private CellStyle getBoldCellStyle() {
        if (boldCellStyle == null) {
            boldCellStyle = workbook.createCellStyle();
            boldCellStyle.setFont(getBoldFont());
            setBorders(boldCellStyle);
        }

        return boldCellStyle;
    }

    private CellStyle getBoldDigitCellStyle() {
        if (boldDigitCellStyle == null) {
            boldDigitCellStyle = workbook.createCellStyle();
            boldDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            boldDigitCellStyle.setFont(getBoldFont());
            setBorders(boldDigitCellStyle);
        }

        return boldDigitCellStyle;
    }

    public CellStyle getTotalCellStyle() {
        if (totalCellStyle == null) {
            totalCellStyle = workbook.createCellStyle();
            totalCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            totalCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            totalCellStyle.setFont(getHeaderFont());
            totalCellStyle.setAlignment(HorizontalAlignment.RIGHT);
            setBorders(totalCellStyle);
        }

        return totalCellStyle;
    }

    private CellStyle getLightCellStyle() {
        if (lightCellStyle == null) {
            lightCellStyle = workbook.createCellStyle();
            lightCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            lightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            lightCellStyle.setFont(getBoldFont());
            lightCellStyle.setAlignment(HorizontalAlignment.LEFT);
            setBorders(lightCellStyle);
        }

        return lightCellStyle;
    }

    private CellStyle getLightHeaderCellStyle() {
        if (lightHeaderCellStyle == null) {
            lightHeaderCellStyle = workbook.createCellStyle();
            lightHeaderCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            lightHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            lightHeaderCellStyle.setFont(getBoldFont());
            lightHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            lightHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            lightHeaderCellStyle.setWrapText(true);
            setBorders(lightHeaderCellStyle);
        }

        return lightHeaderCellStyle;
    }

    private CellStyle getLightTotalCellStyle() {
        if (lightTotalCellStyle == null) {
            lightTotalCellStyle = workbook.createCellStyle();
            lightTotalCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            lightTotalCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            lightTotalCellStyle.setFont(getBoldFont());
            lightTotalCellStyle.setAlignment(HorizontalAlignment.RIGHT);
            setBorders(lightTotalCellStyle);
        }

        return lightTotalCellStyle;
    }

    private CellStyle getLightDigitCellStyle() {
        if (lightDigitCellStyle == null) {
            lightDigitCellStyle = workbook.createCellStyle();
            lightDigitCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            lightDigitCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            lightDigitCellStyle.setFont(getBoldFont());
            lightDigitCellStyle.setAlignment(HorizontalAlignment.CENTER);
            setBorders(lightDigitCellStyle);
        }

        return lightDigitCellStyle;
    }

    private CellStyle getNonBorderCellStyle() {
        if (nonBorderCellStyle == null) {
            nonBorderCellStyle = workbook.createCellStyle();
            nonBorderCellStyle.setFont(getDefaultFont());
            nonBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return nonBorderCellStyle;
    }

    private CellStyle getNonBorderDigitCellStyle() {
        if (nonBorderDigitCellStyle == null) {
            nonBorderDigitCellStyle = workbook.createCellStyle();
            nonBorderDigitCellStyle.setFont(getDefaultFont());
            nonBorderDigitCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return nonBorderDigitCellStyle;
    }

    private CellStyle getNonBorderMarkedCellStyle() {
        if (nonBorderMarkedCellStyle == null) {
            nonBorderMarkedCellStyle = workbook.createCellStyle();
            nonBorderMarkedCellStyle.setFont(getHeaderFont());
            nonBorderMarkedCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            nonBorderMarkedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            nonBorderMarkedCellStyle.setAlignment(HorizontalAlignment.LEFT);
        }

        return nonBorderMarkedCellStyle;
    }

    private CellStyle getNonBorderHeaderCellStyle() {
        if (nonBorderHeaderCellStyle == null) {
            nonBorderHeaderCellStyle = workbook.createCellStyle();
            nonBorderHeaderCellStyle.setFont(getMainHeaderFont());
            nonBorderHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            nonBorderHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }

        return nonBorderHeaderCellStyle;
    }

    private void setBorders(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    public Resource save(String path) throws IOException {
        File file = new File(path);
        FileOutputStream outFile = new FileOutputStream(file);

        changeColors();
        workbook.write(outFile);
        workbook.close();

        return new FileSystemResource(path);
    }

    private void changeColors() {
        HSSFPalette palette = workbook.getCustomPalette();
        palette.setColorAtIndex(IndexedColors.AQUA.getIndex(), (byte) 0, (byte) 142, (byte) 140);
        palette.setColorAtIndex(IndexedColors.GREY_40_PERCENT.getIndex(), (byte) 232, (byte) 232, (byte) 232);
    }
}

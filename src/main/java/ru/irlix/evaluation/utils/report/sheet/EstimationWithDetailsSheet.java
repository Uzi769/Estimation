package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

@RequiredArgsConstructor
public class EstimationWithDetailsSheet implements Sheet {

    private final ExcelWorkbook helper;

    @Override
    public XSSFSheet getSheet(Estimation estimation) {
        XSSFSheet sheet = helper.getWorkbook().createSheet("Estimations");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0 ,2));

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
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Специалист", 1);
        helper.setHeaderCell(row, "Часы (мин)", 2);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 3);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 4);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 5);
        helper.setHeaderCell(row, "Комментарии", 6);

//        for (Estimation estimation : estimations) {
//            rowNum++;
//            row = sheet.createRow(rowNum);
//            row.setHeight((short) 350);
//
//            setCell(row, estimation.getId(), 0);
//            setCell(row, estimation.getName(), 1);
//            setCell(row, estimation.getCreateDate(), 2);
//
//            if (estimation.getRisk() != null) {
//                setCell(row, estimation.getRisk(), 3);
//            }
//
//            setCell(row, estimation.getStatus().getDisplayValue(), 4);
//            setCell(row, estimation.getClient(), 5);
//            setCell(row, estimation.getCreator(), 6);
//        }

        return sheet;
    }
}

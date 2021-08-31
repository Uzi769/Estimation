package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.utils.report.sheet.EstimationWithDetailsSheet;
import ru.irlix.evaluation.utils.report.sheet.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportHelper {

    public Resource getEstimationReportResource(Estimation estimation) throws IOException {
        List<Sheet> sheets = new ArrayList<>();
        ExcelWorkbook excelWorkbook = new ExcelWorkbook();

        sheets.add(new EstimationWithDetailsSheet(excelWorkbook));

        sheets.forEach(s -> s.getSheet(estimation));

        File file = new File("C:/output/estimations.xlsx");

        FileOutputStream outFile = new FileOutputStream(file);
        excelWorkbook.getWorkbook().write(outFile);
        excelWorkbook.getWorkbook().close();
        excelWorkbook.setWorkbook(new XSSFWorkbook());

        return new FileSystemResource("C:/output/estimations.xlsx");
    }
}

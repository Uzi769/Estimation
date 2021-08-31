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
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportHelper {

    private final ExcelHelper excelHelper;

    public Resource getEstimationReportResource(Estimation estimation) throws IOException {
        List<Sheet> sheets = List.of(
          new EstimationWithDetailsSheet(excelHelper)
        );

        sheets.forEach(s -> s.getSheet(estimation));

        File file = new File("C:/output/estimations.xlsx");

        FileOutputStream outFile = new FileOutputStream(file);
        excelHelper.getWorkbook().write(outFile);
        excelHelper.getWorkbook().close();
        excelHelper.setWorkbook(new XSSFWorkbook());

        return new FileSystemResource("C:/output/estimations.xlsx");
    }
}

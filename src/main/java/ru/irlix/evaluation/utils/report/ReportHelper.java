package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.ReportRequest;
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

    public Resource getEstimationReportResource(Estimation estimation, ReportRequest request) throws IOException {
        ExcelWorkbook excelWorkbook = new ExcelWorkbook();

        List<Sheet> sheets = new ArrayList<>();
        sheets.add(new EstimationWithDetailsSheet(excelWorkbook));
        sheets.forEach(s -> s.getSheet(estimation, request));

        return excelWorkbook.save("C:/output/estimations.xlsx");
    }
}

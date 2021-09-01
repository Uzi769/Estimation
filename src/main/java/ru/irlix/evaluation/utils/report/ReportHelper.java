package ru.irlix.evaluation.utils.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.sheet.EstimationWithDetailsSheet;
import ru.irlix.evaluation.utils.report.sheet.EstimationWithoutDetailsSheet;
import ru.irlix.evaluation.utils.report.sheet.PhaseEstimationSheet;
import ru.irlix.evaluation.utils.report.sheet.Sheet;
import ru.irlix.evaluation.utils.report.sheet.FeatureSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportHelper {

    @Value("${document-path}")
    private String path;

    public Resource getEstimationReportResource(Estimation estimation, ReportRequest request) throws IOException {
        ExcelWorkbook excelWorkbook = new ExcelWorkbook();

        List<Sheet> sheets = new ArrayList<>();
        sheets.add(new EstimationWithDetailsSheet(excelWorkbook));
        sheets.add(new EstimationWithoutDetailsSheet(excelWorkbook));
        sheets.add(new FeatureSheet(excelWorkbook));
        sheets.add(new PhaseEstimationSheet(excelWorkbook));

        sheets.forEach(s -> s.getSheet(estimation, request));

        return excelWorkbook.save(path);
    }
}

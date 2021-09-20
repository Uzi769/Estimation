package ru.irlix.evaluation.utils.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.config.UTF8Control;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.LocaleConstants;
import ru.irlix.evaluation.utils.report.sheet.EstimationWithDetailsSheet;
import ru.irlix.evaluation.utils.report.sheet.EstimationWithoutDetailsSheet;
import ru.irlix.evaluation.utils.report.sheet.PhaseEstimationSheet;
import ru.irlix.evaluation.utils.report.sheet.EstimationReportSheet;
import ru.irlix.evaluation.utils.report.sheet.TasksByRolesSheet;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ReportHelper {

    @Value("${document-path}")
    private String path;

    private final ResourceBundle messageBundle = ResourceBundle.getBundle(
            "messages",
            LocaleConstants.DEFAULT_LOCALE,
            new UTF8Control()
    );

    public Resource getEstimationReportResource(Estimation estimation, ReportRequest request) throws IOException {
        ExcelWorkbook excelWorkbook = new ExcelWorkbook();

        List<EstimationReportSheet> sheets = new ArrayList<>();
        sheets.add(new EstimationWithDetailsSheet(excelWorkbook));
        sheets.add(new EstimationWithoutDetailsSheet(excelWorkbook));
        sheets.add(new TasksByRolesSheet(excelWorkbook));
        sheets.add(new PhaseEstimationSheet(excelWorkbook));

        sheets.forEach(s -> s.getSheet(estimation, request));

        String fileName = messageBundle.getString("estimation.string")
                + " " + estimation.getClient() + " " + estimation.getName() + ".xls";
        String filePath = Paths.get(path, fileName).toString();
        return excelWorkbook.save(filePath);
    }
}

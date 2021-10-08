package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.sheet.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportHelper {

    @Value("${document-path}")
    private String path;

    private final EstimationMath math;

    public Resource getEstimationReportResource(Estimation estimation, Map<String, String> request) throws IOException {
        ExcelWorkbook excelWorkbook = new ExcelWorkbook();

        List<EstimationReportSheet> sheets = new ArrayList<>();
        sheets.add(new EstimationWithDetailsSheet(math, excelWorkbook));
        sheets.add(new EstimationWithoutDetailsSheet(math, excelWorkbook));
        sheets.add(new TasksByRolesSheet(math, excelWorkbook));
        sheets.add(new PhaseEstimationSheet(math, excelWorkbook));

        List<String> roleCosts = sheets.get(0).getRoleCosts(estimation, request);
        if (!request.keySet().containsAll(roleCosts)) {
            throw new IllegalArgumentException("Costs are not shown for all roles.");
        }

        sheets.forEach(s -> s.getSheet(estimation, request));

        String filePath = Paths.get(path, "report.xls").toString();
        return excelWorkbook.save(filePath);
    }
}

package ru.irlix.evaluation.utils.report.sheet;

import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.ReportRequest;

public interface Sheet {
    void getSheet(Estimation estimation, ReportRequest request);
}

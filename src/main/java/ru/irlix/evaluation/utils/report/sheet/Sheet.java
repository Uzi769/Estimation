package ru.irlix.evaluation.utils.report.sheet;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;

public interface Sheet {
    XSSFSheet getSheet(Estimation estimation);
}

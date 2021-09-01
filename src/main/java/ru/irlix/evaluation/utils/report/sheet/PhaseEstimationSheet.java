package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

@RequiredArgsConstructor
public class PhaseEstimationSheet implements Sheet {

    private final ExcelWorkbook helper;

    private int rowNum = 0;

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        XSSFSheet sheet = helper.getWorkbook().createSheet("Оценка по фазам");

        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 6000);

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 1050);
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 1);
        helper.setHeaderCell(row, "Стоимость, RUB", 2);

        for (Phase phase : estimation.getPhases()) {
            rowNum++;
            row = sheet.createRow(rowNum);
            helper.setPhaseCell(row, phase.getName(), 0);

            double sumHoursMax = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMax() * 60).sum()
                            : t.getHoursMax() * 60)
                    .sum();

            helper.setPhaseCell(row, sumHoursMax, 1);

            double costMax = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMax() * getCost(request, nt)).sum()
                            : t.getHoursMax() * getCost(request, t)

                    ).sum();

            helper.setPhaseCell(row, costMax, 2);
        }

    }

    private double getCost(ReportRequest request, Task t) {
        double roleCost = 0;
        switch (t.getRole().getDisplayValue()) {
            case "Аналитик":
                roleCost = request.getAnalystCost();
                break;
            case "Дизайнер":
                roleCost = request.getDesignCost();
                break;
            case "Тестировщик":
                roleCost = request.getQaCost();
                break;
            case "Разработчик":
                roleCost = request.getDevCost();
                break;
        }
        return roleCost;
    }
}

package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EstimationWithDetailsSheet implements Sheet {

    private final ExcelWorkbook helper;
    private double hoursMinSummary;
    private double hoursMaxSummary;
    private double costMinSummary;
    private double costMaxSummary;

    @Override
    public XSSFSheet getSheet(Estimation estimation) {
        XSSFSheet sheet = helper.getWorkbook().createSheet("Estimations");

        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 16000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 12000);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        int rowNum = 0;

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 1050);
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Специалист", 3);
        helper.setHeaderCell(row, "Часы (мин)", 4);
        helper.setHeaderCell(row, "Стоимость (мин), RUB", 5);
        helper.setHeaderCell(row, "Часы (наиболее вероятные)", 6);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 7);
        helper.setHeaderCell(row, "Комментарии", 8);

        for (Phase phase : estimation.getPhases()) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeight((short) 380);

            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));

            helper.setPhaseCell(row, phase.getName(), 0);

            helper.setPhaseCell(row, null, 3);

            double sumHoursMin = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMin() * 60).sum()
                            : t.getHoursMin() * 60)
                    .sum();

            hoursMinSummary += sumHoursMin;
            helper.setPhaseCell(row, sumHoursMin, 4);

            double sumCostMin = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMin() * 120).sum()
                            : t.getHoursMin() * 120)
                    .sum();

            costMinSummary += sumCostMin;
            helper.setPhaseCell(row, sumCostMin, 5);

            double sumHoursMax = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMax() * 60).sum()
                            : t.getHoursMax() * 60)
                    .sum();

            hoursMaxSummary += sumHoursMax;
            helper.setPhaseCell(row, sumHoursMax, 6);

            double sumCostMax = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMax() * 120).sum()
                            : t.getHoursMax() * 120)
                    .sum();

            costMaxSummary += sumCostMax;
            helper.setPhaseCell(row, sumCostMax, 7);

            helper.setPhaseCell(row, null, 8);

            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> t.getParent() == null)
                    .collect(Collectors.toList());

            for (Task task : tasks) {
                rowNum++;
                row = sheet.createRow(rowNum);
                row.setHeight((short) 370);

                if (EntityConstants.FEATURE_ID.equals(task.getType().getId())) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));

                    helper.setBoldCell(row, task.getName(), 1);

                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMin() * 60)
                            .sum(), 4);

                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMin() * 120)
                            .sum(), 5);

                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMax() * 60)
                            .sum(), 6);

                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMax() * 120)
                            .sum(), 7);

                    helper.setCell(row, task.getComment(), 8);

                    for (Task nestedTask : task.getTasks()) {
                        rowNum++;
                        row = sheet.createRow(rowNum);
                        row.setHeight((short) 370);

                        helper.setCell(row, task.getName(), 2);
                        fillTaskRow(row, nestedTask);
                    }

                } else if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
                    helper.setCell(row, task.getName(), 1);
                    fillTaskRow(row, task);
                }
            }
        }
        
        rowNum++;
        row = sheet.createRow(rowNum);
        row.setHeight((short) 380);
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));
        helper.setPhaseCell(row, "Итого по проекту:", 0);
        helper.setPhaseCell(row, hoursMinSummary, 4);
        helper.setPhaseCell(row, costMinSummary, 5);
        helper.setPhaseCell(row, hoursMaxSummary, 6);
        helper.setPhaseCell(row, costMaxSummary, 7);
        helper.setPhaseCell(row, null, 8);


        return sheet;
    }

    private void fillTaskRow(Row row, Task task) {
        helper.setCell(row, task.getRole() != null ? task.getRole().getDisplayValue() : null, 3);

        helper.setCell(row, task.getHoursMin() * 60, 4);

        helper.setCell(row, task.getHoursMin() * 120, 5);

        helper.setCell(row, task.getHoursMax() * 60, 6);

        helper.setCell(row, task.getHoursMax() * 120, 7);

        helper.setCell(row, task.getComment(), 8);
    }
}

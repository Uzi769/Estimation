package ru.irlix.evaluation.utils.report.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;
import ru.irlix.evaluation.utils.report.ExcelWorkbook;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EstimationWithoutDetailsSheet implements Sheet {

    private final ExcelWorkbook helper;
    private double hoursMinSummary;
    private double hoursMaxSummary;
    private double costMinSummary;
    private double costMaxSummary;

    @Override
    public void getSheet(Estimation estimation, ReportRequest request) {
        XSSFSheet sheet = helper.getWorkbook().createSheet("Оценка без детализации");

        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 12000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 10000);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        int rowNum = 0;

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 1050);
        helper.setHeaderCell(row, "Задачи", 0);
        helper.setHeaderCell(row, "Часы (мин)", 3);
        helper.setHeaderCell(row, "Стоимость, RUB", 4);
        helper.setHeaderCell(row, "Часы (мин, наиболее вероятные)", 5);
        helper.setHeaderCell(row, "Стоимость (наиболее вероятная)", 6);
        helper.setHeaderCell(row, "Комментарии", 7);

        //Фазы:
        for (Phase phase : estimation.getPhases()) {
            rowNum++;
            row = sheet.createRow(rowNum);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));
            helper.setPhaseCell(row, phase.getName(), 0);

            //sumHoursMin:
            double sumHoursMin = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMin() * 60).sum() //задачи
                            : t.getHoursMin() * 60) //фичи
                    .sum();
            hoursMinSummary += sumHoursMin;
            helper.setPhaseCell(row, sumHoursMin, 3);

            //minCost: - ставка из модели входных данных
            helper.setPhaseCell(row, 0, 4);

            //sumHoursMax:
            double sumHoursMax = phase.getTasks().stream()
                    .mapToDouble(t -> !t.getTasks().isEmpty()
                            ? t.getTasks().stream().mapToDouble(nt -> nt.getHoursMax() * 60).sum()
                            : t.getHoursMax() * 60)
                    .sum();

            hoursMaxSummary += sumHoursMax;
            helper.setPhaseCell(row, sumHoursMax, 5);

            //maxCost: - ставка из модели входных данных
            helper.setPhaseCell(row, 0, 6);

            helper.setPhaseCell(row, null, 7);

            List<Task> tasks = phase.getTasks().stream()
                    .filter(t -> t.getParent() == null)//фичи
                    .collect(Collectors.toList());

            //Фичи:
            for (Task task : tasks) {
                rowNum++;
                row = sheet.createRow(rowNum);
                row.setHeight((short) 370);

                if (EntityConstants.FEATURE_ID.equals(task.getType().getId())) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));

                    helper.setBoldCell(row, task.getName(), 1);

                    //Количество часов (min)
                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMin() * 60)
                            .sum(), 3);

                    //cost min:
                    helper.setCell(row, 0, 4);

                    //Количество часов (max)
                    helper.setCell(row, task.getTasks().stream()
                            .mapToDouble(t -> t.getHoursMax() * 60)
                            .sum(), 5);


                    //cost max:
                    helper.setCell(row, 0, 6);

                    helper.setCell(row, task.getComment(), 7);

                    //Задачи:
                    for (Task nestedTask : task.getTasks()) {
                        rowNum++;
                        row = sheet.createRow(rowNum);
                        row.setHeight((short) 370);

                        helper.setCell(row, nestedTask.getName(), 2);
                        fillTaskRow(row, nestedTask);
                    }
                }
                //Задачи без фич:
                else if (EntityConstants.TASK_ID.equals(task.getType().getId())) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
                    helper.setCell(row, task.getName(), 1);
                    fillTaskRow(row, task);
                }

            }
        }

        rowNum++;
        row = sheet.createRow(rowNum);
        row.setHeight((short) 380);

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));
        helper.setTotalCell(row, "Итого по проекту:", 0);
        helper.setPhaseCell(row, hoursMinSummary, 3);
        helper.setPhaseCell(row, costMinSummary, 4);
        helper.setPhaseCell(row, hoursMaxSummary, 5);
        helper.setPhaseCell(row, costMaxSummary, 6);
        helper.setPhaseCell(row, null, 7);

    }

    private void fillTaskRow(Row row, Task task) {
        helper.setCell(row, task.getHoursMin() * 60, 3);

        helper.setCell(row, 0, 4);

        helper.setCell(row, task.getHoursMax() * 60, 5);

        helper.setCell(row, 0, 6);

        helper.setCell(row, task.getComment(), 7);
    }
}

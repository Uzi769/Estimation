package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationStatsResponse {

    private String value;

    private double minHoursRange;

    private double maxHoursRange;

    private double minHoursPert;

    private double maxHoursPert;
}

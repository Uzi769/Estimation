package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationStatsResponse {

    private RoleResponse role;

    private double minHoursRange;

    private double maxHoursRange;

    private double minHoursPert;

    private double maxHoursPert;
}

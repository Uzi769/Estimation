package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhaseStatsResponse {

    private String value;

    private double minHours;

    private double maxHours;

    private double bugfixMinHours;

    private double bugfixMaxHours;

    private double qaMinHours;

    private double qaMaxHours;

    private double pmMinHours;

    private double pmMaxHours;

    private double sumMinHours;

    private double sumMaxHours;
}

package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    private boolean pert;

    private double qaCost;

    private double analystCost;

    private double devCost;

    private double pmCost;
    
    private double designCost;
}

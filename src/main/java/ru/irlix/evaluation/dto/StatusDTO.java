package ru.irlix.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatusDTO {
    private Long id;
    private String value;
    private String displayValue;
    private List<EstimateDTO> estimates;
}

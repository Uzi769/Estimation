package ru.irlix.evaluation.dto;

import lombok.Data;

@Data
public class PhaseDTO {
    private Long id;
    private String name;
    private Integer estimate;
    private Integer sortOrder;
    private Integer managementReserve;
    private Integer bagsReserve;
    private Integer qaReserve;
    private Integer riskReserve;
    private String estimateRole;
}

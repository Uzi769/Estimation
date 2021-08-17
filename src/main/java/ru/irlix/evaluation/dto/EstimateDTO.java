package ru.irlix.evaluation.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class EstimateDTO {
    private String name;
    private Instant createDate;
    private String description;
    private Integer risk;
    private String client;
    private String creator;
    private List<PhaseDTO> phases;
}

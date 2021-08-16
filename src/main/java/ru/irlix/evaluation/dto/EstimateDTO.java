package ru.irlix.evaluation.dto;

import lombok.Data;
import ru.irlix.evaluation.dao.entity.Status;

import java.time.Instant;
import java.util.List;

@Data
public class EstimateDTO {
    private Long id;
    private String name;
    private Instant createDate;
    private String description;
    private Integer risk;
    private Status status;
    private String client;
    private String creator;
    private List<PhaseDTO> phases;
}

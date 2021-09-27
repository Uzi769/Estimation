package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationResponse {

    private Long id;

    private String name;

    private Instant createDate;

    private String description;

    private Integer risk;

    private String client;

    private String creator;

    private Long status;

    private Double hoursMin;

    private Double hoursMax;

    private List<PhaseResponse> phases;
}

package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationFilterRequest {

    private String name;

    private String client;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    private String creator;

    private Integer page = 0;

    private Integer size = 25;
}

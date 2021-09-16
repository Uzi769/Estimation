package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationFilterRequest extends EstimationPageRequest {

    private String name;

    private String client;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    private String creator;
}

package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EstimationFilterRequest extends PageableAndSortableRequest {

    private String text;

    private String name;

    private String client;

    private String creator;

    private Long status;

    private Instant beginDate;

    private Instant endDate;
}

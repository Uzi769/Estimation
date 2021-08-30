package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationFilterRequest {

    private String name;

    private String client;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    private String creator;

    @PositiveOrZero(message = "{page.positiveOrZero}")
    private Integer page = 0;

    @Positive(message = "{size.positive}")
    private Integer size = 25;
}

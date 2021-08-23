package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.Instant;
import ru.irlix.evaluation.utils.EntityConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequest {

    private String name;

    private String client;

    private String description;

    private Integer risk;

    @Positive(message = "Status id cant be negative or zero")
    private Long status = EntityConstants.DEFAULT_STATUS_ID;

    private String creator;
}

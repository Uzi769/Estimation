package ru.irlix.evaluation.dto.request;

import lombok.*;
import ru.irlix.evaluation.utils.EntityConstants;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequest {

    private String name;

    private String client;

    private String description;

    private Instant createDate;

    private Integer risk;

    private Long status = EntityConstants.DEFAULT_STATUS_ID;

    private String creator;
}

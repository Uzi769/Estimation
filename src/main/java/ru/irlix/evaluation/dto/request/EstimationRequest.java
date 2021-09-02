package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.irlix.evaluation.utils.EntitiesIdConstants;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequest {

    private String name;

    private String client;

    private String description;

    private Integer risk;

    @Positive(message = "{status.positive}")
    private Long status = EntitiesIdConstants.DEFAULT_STATUS_ID;

    private String creator;
}

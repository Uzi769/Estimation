package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequest {

    private String name;

    private String client;

    @Max(value = 5000, message = "{description.max}")
    private String description;

    private Integer risk;

    @Positive(message = "{status.positive}")
    private Long status = EntitiesIdConstants.DEFAULT_STATUS_ID;

    private String creator;
}

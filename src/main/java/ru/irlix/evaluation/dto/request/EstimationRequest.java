package ru.irlix.evaluation.dto.request;

import lombok.*;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationRequest {

    private String name;

    private String client;

    @Max(value = 5000, message = "{description.max}")
    private String description;

    private Integer risk;

    @Positive(message = "{status.positive}")
    private Long status = EntitiesIdConstants.DEFAULT_STATUS_ID;

    private String creator;

    private List<Long> userIdList;

}

package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EstimationFindAnyRequest extends EstimationPageRequest {

    private String text;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

}

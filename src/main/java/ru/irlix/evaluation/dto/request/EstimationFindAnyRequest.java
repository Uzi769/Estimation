package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationFindAnyRequest {

    private String text;

    private Long status;

    private Instant beginDate;

    private Instant endDate;

    @PositiveOrZero(message = "{page.positiveOrZero}")
    private Integer page = 0;

    @Positive(message = "{size.positive}")
    private Integer size = 25;

    @Pattern(regexp = "^createDate$|^name$|^creator$", message = "{nameSortField.string}")
    private String nameSortField;

    private Boolean sortAsc;
}

package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationPageRequest {
    @PositiveOrZero(message = "{page.positiveOrZero}")
    private Integer page = 0;

    @Positive(message = "{size.positive}")
    private Integer size = 25;

    @Pattern(regexp = "^createDate$|^name$|^creator$", message = "{nameSortField.string}")
    private String nameSortField;

    private Boolean sortAsc;

    private Long userId;
}

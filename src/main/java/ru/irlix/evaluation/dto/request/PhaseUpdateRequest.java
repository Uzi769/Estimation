package ru.irlix.evaluation.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PhaseUpdateRequest extends PhaseRequest {
    private Long id;

}

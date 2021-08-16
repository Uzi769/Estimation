package ru.irlix.evaluation.dao.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long id;
    private String value;
    private String displayValue;
}

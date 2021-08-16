package ru.irlix.evaluation.dao.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhaseResponse {
    private Long id;
    private String name;
    private String estimateName;
    private Integer sortOrder;
    private Integer managementReserve;
    private Integer bagsReserve;
    private Integer qaReserve;
    private Integer riskReserve;
    private RoleResponse role;
}

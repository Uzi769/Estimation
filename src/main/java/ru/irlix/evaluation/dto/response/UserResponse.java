package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponse {
    private Long userId;
    private String name;
}

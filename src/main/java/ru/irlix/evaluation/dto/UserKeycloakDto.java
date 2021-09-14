package ru.irlix.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserKeycloakDto {
    private UUID id;
    private String firstName;
    private String LastName;

}

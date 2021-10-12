package ru.irlix.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserKeycloakDto {

    private UUID id;

    private String firstName;

    private String LastName;
}

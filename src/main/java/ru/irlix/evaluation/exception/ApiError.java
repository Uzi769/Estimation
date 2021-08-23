package ru.irlix.evaluation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
    }

    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
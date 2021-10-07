package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.service.FileStorageService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/files")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        return new ResponseEntity<>(fileStorageService.loadFileAsResource(id), HttpStatus.OK);
    }
}

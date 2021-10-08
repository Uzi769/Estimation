package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.service.FileStorageService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import javax.validation.constraints.Positive;

@Log4j2
@RequestMapping(UrlConstants.BASE_URL + "/files")
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return new ResponseEntity<>(fileStorageService.loadFileAsResource(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        fileStorageService.deleteFile(id);
    }
}

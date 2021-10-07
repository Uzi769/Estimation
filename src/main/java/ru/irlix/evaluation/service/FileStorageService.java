package ru.irlix.evaluation.service;

import org.springframework.core.io.Resource;

public interface FileStorageService {
    Resource loadFileAsResource(Long id);
}

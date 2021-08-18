package ru.irlix.evaluation.service.service;

import ru.irlix.evaluation.dao.entity.Status;

public interface StatusService {
    Status findByName(String name);
}

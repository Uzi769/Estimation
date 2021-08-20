package ru.irlix.evaluation.service.status;

import ru.irlix.evaluation.dao.entity.Status;

public interface StatusService {
    Status findById(Long id);
}

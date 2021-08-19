package ru.irlix.evaluation.service.status;

import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;

import java.util.List;

public interface StatusService {
    StatusResponse createStatus(StatusRequest statusRequest);

    StatusResponse updateStatus(Long id, StatusRequest statusRequest);

    void deleteStatusById(Long id);

    StatusResponse findStatusResponseById(Long id);

    Status findStatusById(Long id);

    List<StatusResponse> findAll();

    Status findByName(String name);
}

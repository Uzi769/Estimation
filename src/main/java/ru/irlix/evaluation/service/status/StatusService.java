package ru.irlix.evaluation.service.status;

import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;

import java.util.List;

public interface StatusService {
    StatusResponse createStatus(StatusRequest statusRequest);

    StatusResponse updateStatus(Long id, StatusRequest statusRequest);

    void deleteStatus(Long id);

    StatusResponse findStatusResponseById(Long id);

    List<StatusResponse> findAllStatuses();
}

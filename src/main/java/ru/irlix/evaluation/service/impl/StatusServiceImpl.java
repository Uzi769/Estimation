package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.StatusMapper;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.repository.StatusRepository;
import ru.irlix.evaluation.service.StatusService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper mapper;

    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        Status status = mapper.statusRequestToStatus(statusRequest);
        Status savedStatus = statusRepository.save(status);
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    public StatusResponse updateStatus(Long id, StatusRequest statusRequest) {
        Status status = findStatusById(id);
        checkAndUpdateFields(status, statusRequest);
        Status savedStatus = statusRepository.save(status);
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    public void deleteStatus(Long id) {
        Status status = findStatusById(id);
        statusRepository.delete(status);
    }

    @Override
    public StatusResponse findStatusResponseById(Long id) {
        Status status = findStatusById(id);
        return mapper.statusToStatusResponse(status);
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
    }

    @Override
    public List<StatusResponse> findAllStatuses() {
        List<Status> statusList = statusRepository.findAll();
        return mapper.statusesToStatusResponseList(statusList);
    }

    private void checkAndUpdateFields(Status status, StatusRequest statusRequest) {
        if (statusRequest.getValue() != null) {
            status.setValue(statusRequest.getValue());
        }
        if (statusRequest.getDisplayValue() != null) {
            status.setDisplayValue(statusRequest.getDisplayValue());
        }
    }
}

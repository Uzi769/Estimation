package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.StatusMapper;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.repository.StatusRepository;
import ru.irlix.evaluation.service.StatusService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper mapper;

    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        Status status = mapper.statusRequestToStatus(statusRequest);
        Status savedStatus = statusRepository.save(status);
        log.info("Method createStatus: Status saved");
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    public StatusResponse updateStatus(Long id, StatusRequest statusRequest) {
        Status status = findStatusById(id);
        checkAndUpdateFields(status, statusRequest);
        Status savedStatus = statusRepository.save(status);
        log.info("Method updateStatus: Status updated");
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    public void deleteStatus(Long id) {
        Status status = findStatusById(id);
        statusRepository.delete(status);
        log.info("Method deleteStatus: Status deleted");
    }

    @Override
    public StatusResponse findStatusResponseById(Long id) {
        Status status = findStatusById(id);
        log.info("Method findStatusResponseById: Found statusResponse by id");
        return mapper.statusToStatusResponse(status);
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findStatusById: Status with id " + id + " not found");
                    return new NotFoundException("Status with id " + id + " not found");
                });
    }

    @Override
    public List<StatusResponse> findAllStatuses() {
        List<Status> statusList = statusRepository.findAll();
        log.info("Method findAllStatuses: Found all statuses");
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

package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public StatusResponse createStatus(StatusRequest statusRequest) {
        Status status = mapper.statusRequestToStatus(statusRequest);
        Status savedStatus = statusRepository.save(status);

        log.info("Status with id " + savedStatus.getId() + " saved");
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    @Transactional
    public StatusResponse updateStatus(Long id, StatusRequest statusRequest) {
        Status status = findStatusById(id);
        checkAndUpdateFields(status, statusRequest);
        Status savedStatus = statusRepository.save(status);

        log.info("Status with id " + savedStatus.getId() + " updated");
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    @Transactional
    public void deleteStatus(Long id) {
        Status status = findStatusById(id);
        statusRepository.delete(status);
        log.info("Status with id " + status.getId() + " deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public StatusResponse findStatusResponseById(Long id) {
        Status status = findStatusById(id);
        log.info("Status with id " + status.getId() + " found");
        return mapper.statusToStatusResponse(status);
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponse> findAllStatuses() {
        List<Status> statusList = statusRepository.findAll();
        log.info("All statuses found");
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

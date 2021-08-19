package ru.irlix.evaluation.service.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.StatusMapper;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.repository.StatusRepository;

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
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
        Status updatedStatus = checkAndUpdateFields(status, statusRequest);
        Status savedStatus = statusRepository.save(updatedStatus);
        return mapper.statusToStatusResponse(savedStatus);
    }

    @Override
    public void deleteStatusById(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
        statusRepository.delete(status);
    }

    @Override
    public StatusResponse findStatusResponseById(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
        return mapper.statusToStatusResponse(status);
    }

    @Override
    public Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
    }

    @Override
    public List<StatusResponse> findAll() {
        List<Status> statusList = statusRepository.findAll();
        return mapper.statusesToStatusResponseList(statusList);
    }

    @Override
    public Status findByName(String name) {
        return statusRepository.findByDisplayValue(name)
                .orElseThrow(() -> new NotFoundException("Status with name " + name + " not found"));
    }

    private Status checkAndUpdateFields(Status status, StatusRequest statusRequest) {
        if (statusRequest.getValue() != null) {
            status.setValue(statusRequest.getValue());
        }
        if (statusRequest.getDisplayValue() != null) {
            status.setDisplayValue(statusRequest.getDisplayValue());
        }
        return status;
    }

}

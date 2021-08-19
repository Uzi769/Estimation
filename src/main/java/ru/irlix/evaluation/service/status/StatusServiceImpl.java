package ru.irlix.evaluation.service.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.repository.StatusRepository;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status findByValue(String value) {
        return statusRepository.findByValue(value)
                .orElseThrow(() -> new NotFoundException("Status with value " + value + " not found"));
    }
}

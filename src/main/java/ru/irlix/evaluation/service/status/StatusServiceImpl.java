package ru.irlix.evaluation.service.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.repository.StatusRepository;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status findByName(String name) {
        return statusRepository.findByDisplayValue(name)
                .orElse(null);
    }
}

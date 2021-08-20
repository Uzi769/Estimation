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
    public Status findById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
    }
}

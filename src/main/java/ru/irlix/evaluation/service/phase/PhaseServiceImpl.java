package ru.irlix.evaluation.service.phase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.repository.PhaseRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository repository;
    private final PhaseMapper mapper;

    @Override
    public void createPhases(Set<Phase> phases) {
        repository.saveAll(phases);
    }
}

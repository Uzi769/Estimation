package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.repository.PhaseRepository;

@Component
@RequiredArgsConstructor
public class PhaseHelper {

    private final PhaseRepository phaseRepository;

    public Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Phase with id " + id + " not found"));
    }
}

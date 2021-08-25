package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.repository.PhaseRepository;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PhaseHelper {

    private final PhaseRepository phaseRepository;

    public Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findPhaseById: Phase with id " + id + " not found");
                    return new NotFoundException("Phase with id " + id + " not found");
                });
    }
}

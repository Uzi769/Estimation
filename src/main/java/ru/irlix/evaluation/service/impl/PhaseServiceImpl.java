package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.repository.PhaseRepository;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.PhaseService;

import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final EstimationRepository estimationRepository;
    private final PhaseMapper mapper;

    @Override
    @Transactional
    public PhaseResponse createPhase(PhaseRequest phaseRequest) {
        Phase phase = mapper.phaseRequestToPhase(phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);
        log.info("Method createPhase: Phase saved");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @Override
    @Transactional
    public PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest) {
        Phase phase = findPhaseById(id);
        checkAndUpdateFields(phase, phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);
        log.info("Method updatePhase: Phase updated");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    private void checkAndUpdateFields(Phase phase, PhaseRequest phaseRequest) {
        if (phaseRequest.getName() != null) {
            phase.setName(phaseRequest.getName());
        }

        if (phaseRequest.getEstimationId() != null) {
            Estimation estimation = findEstimationById(phaseRequest.getEstimationId());
            phase.setEstimation(estimation);
        }

        if (phaseRequest.getSortOrder() != null) {
            phase.setSortOrder(phaseRequest.getSortOrder());
        }

        if (phaseRequest.getManagementReserve() != null) {
            phase.setManagementReserve(phaseRequest.getManagementReserve());
        }

        if (phaseRequest.getQaReserve() != null) {
            phase.setQaReserve(phaseRequest.getQaReserve());
        }

        if (phaseRequest.getBagsReserve() != null) {
            phase.setBagsReserve(phaseRequest.getBagsReserve());
        }

        if (phaseRequest.getRiskReserve() != null) {
            phase.setRiskReserve(phaseRequest.getRiskReserve());
        }

        if (phaseRequest.getDone() != null) {
            phase.setDone(phaseRequest.getDone());
        }

        if (phaseRequest.getManagementReserveOn() != null) {
            phase.setManagementReserveOn(phaseRequest.getManagementReserveOn());
        }

        if (phaseRequest.getQaReserveOn() != null) {
            phase.setQaReserveOn(phaseRequest.getQaReserveOn());
        }

        if (phaseRequest.getBagsReserveOn() != null) {
            phase.setBagsReserveOn(phaseRequest.getBagsReserveOn());
        }
        if (phaseRequest.getRiskReserveOn() != null) {
            phase.setRiskReserveOn(phaseRequest.getRiskReserveOn());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PhaseResponse findPhaseResponseById(Long id) {
        Phase phase = findPhaseById(id);
        log.info("Method findPhaseResponseById: Found phaseResponse by Estimation id");
        return mapper.phaseToPhaseResponse(phase);
    }

    @Override
    @Transactional
    public void deletePhase(Long id) {
        Phase phase = findPhaseById(id);
        phaseRepository.delete(phase);
        log.info("Method deletePhase: Phase deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseResponse> findPhasesByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        Set<Phase> phases = estimation.getPhases();
        log.info("Method findPhasesByEstimationId: Found phase by Estimation id");
        return mapper.phaseToPhaseResponse(phases);
    }

    private Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findPhaseById: Phase with id " + id + " not found");
                    return new NotFoundException("Phase with id " + id + " not found");
                });
    }

    private Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findEstimationById: Estimation with id " + id + " not found");
                    return new NotFoundException("Estimation with id " + id + " not found");
                });
    }
}

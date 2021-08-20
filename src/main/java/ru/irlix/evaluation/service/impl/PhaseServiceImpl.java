package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.repository.PhaseRepository;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.PhaseService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final EstimationRepository estimationRepository;
    private final PhaseMapper mapper;

    @Override
    public PhaseResponse createPhase(PhaseRequest phaseRequest) {
        Phase phase = mapper.phaseRequestToPhase(phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);

        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @Override
    public PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest) {
        Phase phase = findPhaseById(id);
        checkAndUpdateFields(phase, phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);

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
    public PhaseResponse findPhaseResponseById(Long id) {
        Phase phase = findPhaseById(id);
        return mapper.phaseToPhaseResponse(phase);
    }

    @Override
    public void deletePhase(Long id) {
        Phase phase = findPhaseById(id);
        phaseRepository.delete(phase);
    }

    @Override
    @Transactional
    public Set<PhaseResponse> getPhaseSetByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        Set<Phase> phases = estimation.getPhases();

        return mapper.phaseToPhaseResponse(phases);
    }

    private Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Phase with id " + id + " not found"));
    }

    private Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));
    }
}

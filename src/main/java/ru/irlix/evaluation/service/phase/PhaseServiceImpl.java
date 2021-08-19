package ru.irlix.evaluation.service.phase;

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
import ru.irlix.evaluation.service.estimation.EstimationService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final EstimationService estimationService;
    private final PhaseMapper mapper;

    @Override
    public PhaseResponse createPhase(PhaseRequest phaseRequest) {
        Phase phase = mapper.phaseRequestToPhase(phaseRequest, estimationService);
        Phase savedPhase = phaseRepository.save(phase);

        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @Override
    public PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest) {
        Phase phase = findPhaseById(id);
        Phase updatedPhase = checkAndUpdateFields(phase, phaseRequest);
        Phase savedPhase = phaseRepository.save(updatedPhase);

        return mapper.phaseToPhaseResponse(savedPhase);
    }

    private Phase checkAndUpdateFields(Phase phase, PhaseRequest phaseRequest) {
        if (phaseRequest.getName() != null) {
            phase.setName(phaseRequest.getName());
        }

        if (phaseRequest.getEstimationId() != null) {
            Estimation estimation = estimationService.findEstimationById(phaseRequest.getEstimationId());
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

        return phase;
    }

    @Override
    public PhaseResponse findPhaseResponseById(Long id) {
        Phase phase = findPhaseById(id);
        return mapper.phaseToPhaseResponse(phase);
    }

    @Override
    public Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Phase with id " + id + " not found"));
    }

    @Override
    public void deletePhase(Long id) {
        Phase phase = findPhaseById(id);
        phaseRepository.delete(phase);
    }

    @Override
    @Transactional
    public Set<PhaseResponse> getPhaseSetByEstimationId(Long id) {
        Estimation estimation = estimationService.findEstimationById(id);
        Set<Phase> phases = estimation.getPhases();

        return mapper.phaseToPhaseResponse(phases);
    }


    @Override
    public void createPhases(Set<Phase> phases) {
        phaseRepository.saveAll(phases);
    }
}

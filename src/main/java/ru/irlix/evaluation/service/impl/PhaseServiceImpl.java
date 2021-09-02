package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.PhaseUpdateRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.PhaseRepository;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.PhaseService;

import java.util.List;
import java.util.stream.Collectors;

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

        log.info("Phase with id " + savedPhase.getId() + " saved");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @Override
    @Transactional
    public List<PhaseResponse> createPhases(List<PhaseRequest> phaseRequests) {
        List<Phase> phases = mapper.phaseRequestToPhase(phaseRequests);
        List<Phase> savedPhases = phaseRepository.saveAll(phases);

        return mapper.phaseToPhaseResponse(savedPhases);
    }

    @Override
    @Transactional
    public PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest) {
        Phase phase = updatePhaseById(id, phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);

        log.info("Phase with id " + savedPhase.getId() + " updated");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @Override
    @Transactional
    public List<PhaseResponse> updatePhases(List<PhaseUpdateRequest> phaseRequests) {
        List<Phase> updatedPhases = phaseRequests.stream()
                .map(request -> updatePhaseById(request.getId(), request))
                .collect(Collectors.toList());

        List<Phase> savedPhases = phaseRepository.saveAll(updatedPhases);

        log.info("Phase list updated");
        return mapper.phaseToPhaseResponse(savedPhases);
    }

    private Phase updatePhaseById(Long id, PhaseRequest request) {
        Phase phaseToUpdate = findPhaseById(id);
        checkAndUpdateFields(phaseToUpdate, request);
        return phaseToUpdate;
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
        log.info("Phase with id " + phase.getId() + " found");
        return mapper.phaseToPhaseResponse(phase);
    }

    @Override
    @Transactional
    public void deletePhase(Long id) {
        Phase phase = findPhaseById(id);
        phaseRepository.delete(phase);
        log.info("Phase with id " + phase.getId() + " deleted");
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

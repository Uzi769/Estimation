package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.EventInfo;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.PhaseUpdateRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.PhaseRepository;
import ru.irlix.evaluation.service.PhaseService;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final EstimationHelper estimationHelper;
    private final UserHelper userHelper;
    private final PhaseMapper mapper;

    @EventInfo
    @LogInfo
    @Override
    @Transactional
    public PhaseResponse createPhase(PhaseRequest phaseRequest) {
        Phase phase = mapper.phaseRequestToPhase(phaseRequest);
        checkAccessToEstimation(phase);

        Phase savedPhase = phaseRepository.save(phase);
        log.info("Phase with id " + savedPhase.getId() + " saved");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @LogInfo
    @Override
    @Transactional
    public List<PhaseResponse> createPhases(List<PhaseRequest> phaseRequests) {
        List<Phase> phases = mapper.phaseRequestToPhase(phaseRequests);
        List<Phase> phasesWithAccess = getPhasesWithAccess(phases);
        List<Phase> savedPhases = phaseRepository.saveAll(phasesWithAccess);

        log.info("Phase list saved");
        return mapper.phaseToPhaseResponse(savedPhases);
    }

    @LogInfo
    @Override
    @Transactional
    public PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest) {
        Phase phase = updatePhaseById(id, phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);

        log.info("Phase with id " + savedPhase.getId() + " updated");
        return mapper.phaseToPhaseResponse(savedPhase);
    }

    @LogInfo
    @Override
    @Transactional
    public List<PhaseResponse> updatePhases(List<PhaseUpdateRequest> phaseRequests) {
        List<Phase> updatedPhases = phaseRequests.stream()
                .map(request -> updatePhaseById(request.getId(), request))
                .collect(Collectors.toList());

        List<Phase> phaseWithAccess = getPhasesWithAccess(updatedPhases);
        List<Phase> savedPhases = phaseRepository.saveAll(phaseWithAccess);

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
            Estimation estimation = estimationHelper.findEstimationById(phaseRequest.getEstimationId());
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

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public PhaseResponse findPhaseResponseById(Long id) {
        Phase phase = findPhaseById(id);
        log.info("Phase with id " + phase.getId() + " found");
        return mapper.phaseToPhaseResponse(phase);
    }

    @EventInfo
    @LogInfo
    @Override
    @Transactional
    public void deletePhase(Long id) {
        Phase phase = findPhaseById(id);
        phaseRepository.delete(phase);
        log.info("Phase with id " + phase.getId() + " deleted");
    }

    private Phase findPhaseById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Phase with id " + id + " not found"));

        checkAccessToEstimation(phase);
        return phase;
    }

    private void checkAccessToEstimation(Phase phase) {
        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        if (!SecurityUtils.hasAccessToAllEstimations() && !phase.getEstimation().getUsers().contains(user)) {
            throw new AccessDeniedException("User with id " + keycloakId + " cant get access to estimation");
        }
    }

    private List<Phase> getPhasesWithAccess(List<Phase> phases) {
        if (SecurityUtils.hasAccessToAllEstimations()) {
            return phases;
        }

        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        return phases.stream()
                .filter(p -> !p.getEstimation().getUsers().contains(user))
                .collect(Collectors.toList());
    }
}

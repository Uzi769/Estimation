package ru.irlix.evaluation.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.StatusHelper;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.*;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.EstimationService;
import ru.irlix.evaluation.utils.report.ReportHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private final EstimationRepository estimationRepository;
    private final StatusHelper statusHelper;
    private final UserHelper userHelper;
    private final EstimationMapper estimationMapper;
    private final PhaseMapper phaseMapper;
    private final ReportHelper reportHelper;

    @Override
    @Transactional
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = estimationMapper.estimationRequestToEstimation(estimationRequest);
        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        Estimation savedEstimation = estimationRepository.save(estimation);

        savedEstimation.setUsers(new ArrayList<>());
        savedEstimation.getUsers().add(user);

        Estimation savedEstimationWithUser = estimationRepository.save(estimation);

        log.info("Estimation with id " + savedEstimationWithUser.getId() + " saved");
        return estimationMapper.estimationToEstimationResponse(savedEstimationWithUser);
    }

    @Override
    @Transactional
    public EstimationResponse updateEstimation(Long id, EstimationRequest estimationRequest) {
        Estimation estimationToUpdate = findEstimationById(id);
        checkAndUpdateFields(estimationToUpdate, estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimationToUpdate);

        log.info("Estimation with id " + savedEstimation.getId() + " updated");
        return estimationMapper.estimationToEstimationResponse(savedEstimation);
    }

    @Override
    @Transactional
    public void deleteEstimation(Long id) {
        Estimation estimationToDelete = findEstimationById(id);
        estimationRepository.delete(estimationToDelete);
        log.info("Estimation with id " + estimationToDelete.getId() + " deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public EstimationResponse findEstimationResponseById(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Estimation with id " + estimation.getId() + " found");
        return estimationMapper.estimationToEstimationResponse(estimation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhaseResponse> findPhaseResponsesByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Phases of estimation with id " + estimation.getId() + " found");
        return phaseMapper.phaseToPhaseResponse(estimation.getPhases());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstimationResponse> findAllEstimations(EstimationFilterRequest request) {
        addUserIdToRequestIfRequired(request);
        Page<Estimation> estimationList = estimationRepository.filter(request);
        log.info("Estimations filtered and found");
        return estimationMapper.estimationToEstimationResponse(estimationList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstimationResponse> findAnyEstimations(EstimationFindAnyRequest request) {
        addUserIdToRequestIfRequired(request);
        Page<Estimation> estimationList = estimationRepository.findAny(request);
        log.info("Estimations filtered and found");
        return estimationMapper.estimationToEstimationResponse(estimationList);
    }

    private void addUserIdToRequestIfRequired(EstimationPageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasAccessToAllEstimations = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ROLE_SALES"));

        if (!hasAccessToAllEstimations) {
            String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userHelper.findUserByKeycloakId(keycloakId).getUserId();
            request.setUserId(userId);
        }
    }

    private Estimation findEstimationById(Long id) {
        Estimation estimation = estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));

        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        if (!estimation.getUsers().contains(user)) {
            throw new AccessDeniedException("User with id " + keycloakId + " cant get access to estimation");
        }

        return estimation;
    }

    private void checkAndUpdateFields(Estimation estimation, EstimationRequest request) {
        if (request.getName() != null) {
            estimation.setName(request.getName());
        }

        if (request.getClient() != null) {
            estimation.setClient(request.getClient());
        }

        if (request.getDescription() != null) {
            estimation.setDescription(request.getDescription());
        }

        if (request.getRisk() != null) {
            estimation.setRisk(request.getRisk());
        }

        if (request.getStatus() != null) {
            Status status = statusHelper.findStatusById(request.getStatus());
            estimation.setStatus(status);
        }

        if (request.getCreator() != null) {
            estimation.setCreator(request.getCreator());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Resource getEstimationsReport(Long id, ReportRequest request) throws IOException {
        Estimation estimation = findEstimationById(id);
        Resource estimationReport = reportHelper.getEstimationReportResource(estimation, request);

        log.info("Estimation report generated");
        return estimationReport;
    }
}

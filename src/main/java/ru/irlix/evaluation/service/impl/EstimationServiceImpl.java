package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.FileStorageHelper;
import ru.irlix.evaluation.dao.helper.StatusHelper;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.dao.mapper.FileStorageMapper;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationPageRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.FileStorageResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.EstimationService;
import ru.irlix.evaluation.utils.report.ReportHelper;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private final EstimationRepository estimationRepository;
    private final StatusHelper statusHelper;
    private final UserHelper userHelper;
    private final EstimationMapper estimationMapper;
    private final PhaseMapper phaseMapper;
    private final ReportHelper reportHelper;
    private final FileStorageHelper fileStorageHelper;
    private final FileStorageMapper fileStorageMapper;

    @LogInfo
    @Override
    @Transactional
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = estimationMapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimation);

        log.info("Estimation with id " + savedEstimation.getId() + " saved");
        return estimationMapper.estimationToEstimationResponse(savedEstimation);
    }

    @LogInfo
    @Override
    @Transactional
    public EstimationResponse updateEstimation(Long id, EstimationRequest estimationRequest) {
        Estimation estimationToUpdate = findEstimationById(id);
        checkAndUpdateFields(estimationToUpdate, estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimationToUpdate);

        log.info("Estimation with id " + savedEstimation.getId() + " updated");
        return estimationMapper.estimationToEstimationResponse(savedEstimation);
    }

    @LogInfo
    @Override
    @Transactional
    public void deleteEstimation(Long id) {
        Estimation estimationToDelete = findEstimationById(id);
        estimationRepository.delete(estimationToDelete);
        log.info("Estimation with id " + estimationToDelete.getId() + " deleted");
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public EstimationResponse findEstimationResponseById(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Estimation with id " + estimation.getId() + " found");
        return estimationMapper.estimationToEstimationResponse(estimation);
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<PhaseResponse> findPhaseResponsesByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Phases of estimation with id " + estimation.getId() + " found");
        return phaseMapper.phaseToPhaseResponse(estimation.getPhases());
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public Page<EstimationResponse> filterEstimations(EstimationFilterRequest request) {
        addUserIdToRequestIfRequired(request);
        Page<Estimation> estimationList = estimationRepository.filter(request);
        log.info("Estimations filtered and found");
        return estimationMapper.estimationToEstimationResponse(estimationList);
    }

    private void addUserIdToRequestIfRequired(EstimationPageRequest request) {
        if (!SecurityUtils.hasAccessToAllEstimations()) {
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

        if (!SecurityUtils.hasAccessToAllEstimations() && !estimation.getUsers().contains(user)) {
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

        if (request.getUserIdList() != null) {
            estimation.setUsers(userHelper.findByUserIdIn(request.getUserIdList()));
        }

        if (request.getMultipartFiles() != null) {
            fileStorageHelper.storeFileList(request.getMultipartFiles(), estimation);
        }
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<FileStorageResponse> findFileResponsesByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Files of estimation with id " + estimation.getId() + " found");
        return fileStorageMapper.fileStoragesToFileStorageList(estimation.getFileStorages());
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public Resource getEstimationsReport(Long id, Map<String, String> request) throws IOException {
        Estimation estimation = findEstimationById(id);
        Resource estimationReport = reportHelper.getEstimationReportResource(estimation, request);

        log.info("Estimation report generated");
        return estimationReport;
    }
}

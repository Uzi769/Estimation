package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.EventInfo;
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
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.request.PageableAndSortableRequest;
import ru.irlix.evaluation.dto.response.*;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.EstimationService;
import ru.irlix.evaluation.utils.math.EstimationMath;
import ru.irlix.evaluation.utils.report.ReportHelper;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.Period;
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
    private final EstimationMath estimationMath;
    private final FileStorageHelper fileStorageHelper;
    private final FileStorageMapper fileStorageMapper;

    @EventInfo
    @LogInfo
    @Override
    @Transactional
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = estimationMapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimation);

        if (estimationRequest.getFileIds() != null) {
            fileStorageHelper.storeFileListByIds(estimationRequest.getFileIds(), estimation);
        }

        log.info("Estimation with id " + savedEstimation.getId() + " saved");
        return estimationMapper.estimationToEstimationResponse(savedEstimation);
    }

    @EventInfo
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

    @EventInfo
    @LogInfo
    @Override
    @Transactional
    public void setEstimationDeleted(Long id) {
        Estimation estimationToDelete = findEstimationById(id);
        Instant deleteDate = Instant.now().plus(Period.ofDays(7));

        if (estimationToDelete.getDeleteDate() != null) {
            throw new IllegalArgumentException("Estimation with id " + id + " has already been deleted");
        }

        estimationToDelete.setDeleteDate(deleteDate);
        log.info("Estimation with id " + estimationToDelete.getId() + " mark deleted");
    }

    @EventInfo
    @LogInfo
    @Override
    @Transactional
    public void deleteEstimation(Long id) {
        Estimation estimationToDelete = findEstimationById(id);
        fileStorageHelper.deleteFilesByEstimation(estimationToDelete);
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

    @LogInfo
    @EventInfo
    @Override
    @Transactional
    public EstimationResponse restoreEstimation(Long id) {
        Estimation estimationToRestore = findEstimationById(id);

        if (estimationToRestore.getDeleteDate() == null) {
            throw new IllegalArgumentException("Estimation with id " + id + " not deleted");
        }

        estimationToRestore.setDeleteDate(null);
        Estimation estimation = estimationRepository.save(estimationToRestore);
        log.info("Estimation + id + restored");
        return estimationMapper.estimationToEstimationResponse(estimation);
    }

    @Scheduled(cron = "0 10 * * * ?")
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void deleteEstimationFromRecycleBin() {
        EstimationFilterRequest request = new EstimationFilterRequest();
        request.setDeleted(true);
        request.setSize(500);

        List<Estimation> estimations = estimationRepository.filter(request).getContent();
        estimations.forEach(fileStorageHelper::deleteFilesByEstimation);

        estimationRepository.deleteByDeleteDateLessThanEqual(Instant.now());
        log.info("Estimations deleted from recycle bin");
    }

    private void addUserIdToRequestIfRequired(PageableAndSortableRequest request) {
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

    @EventInfo
    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public Resource getEstimationsReport(Long id, Map<String, String> request) throws IOException {
        Estimation estimation = findEstimationById(id);
        Resource estimationReport = reportHelper.getEstimationReportResource(estimation, request);

        log.info("Estimation report by id " + id + " generated");
        return estimationReport;
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<EstimationStatsResponse> getEstimationStats(Long id) {
        Estimation estimation = findEstimationById(id);
        List<EstimationStatsResponse> estimationStats = estimationMath.getEstimationStats(estimation);

        log.info("Estimation stats by id " + id + " calculated");
        return estimationStats;
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public EstimationCostResponse getEstimationCost(Long id, Map<String, String> request) {
        Estimation estimation = findEstimationById(id);
        EstimationCostResponse estimationCost = estimationMath.getEstimationCost(estimation, request);

        log.info("Estimation cost by id" + id + " calculated");
        return estimationCost;
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<FileStorageResponse> findFileResponsesByEstimationId(Long id) {
        Estimation estimation = findEstimationById(id);
        log.info("Files of estimation with id " + estimation.getId() + " found");
        return fileStorageMapper.fileStoragesToFileStorageList(estimation.getFileStorages());
    }

}

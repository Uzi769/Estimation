package ru.irlix.evaluation.service.estimation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.repository.EstimationRepository;
import ru.irlix.evaluation.service.status.StatusService;

import java.util.List;

@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private EstimationRepository estimationRepository;
    private StatusService statusService;
    private EstimationMapper mapper;

    @Override
    public Long createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = mapper.estimationRequestToEstimation(estimationRequest, statusService);
        Estimation savedEstimation = estimationRepository.save(estimation);

        return savedEstimation.getId();
    }

    @Override
    public EstimationResponse updateEstimation(Long id, EstimationRequest estimationRequest) {
        Estimation estimationToUpdate = findEstimationById(id);
        Estimation updatedEstimation = checkAndUpdateFields(estimationToUpdate, estimationRequest);

        Estimation savedEstimation = estimationRepository.save(updatedEstimation);
        return mapper.estimationToEstimationResponse(savedEstimation);
    }

    @Override
    public void deleteEstimation(Long id) {
        Estimation estimationToDelete = findEstimationById(id);
        estimationRepository.delete(estimationToDelete);
    }

    @Override
    public List<EstimationResponse> findAllEstimations(EstimationFilterRequest request) {
        List<Estimation> estimationList = estimationRepository.findAll(request.getPageable()).getContent();
        return mapper.estimationsToEstimationResponseList(estimationList);
    }

    private Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));
    }

    private Estimation checkAndUpdateFields(Estimation estimation, EstimationRequest request) {
        if (request.getName() != null) {
            estimation.setName(request.getName());
        }

        if (request.getClient() != null) {
            estimation.setClient(request.getClient());
        }

        if (request.getDescription() != null) {
            estimation.setDescription(request.getDescription());
        }

        if (request.getCreateDate() != null) {
            estimation.setCreateDate(request.getCreateDate());
        }

        if (request.getRisk() != null) {
            estimation.setRisk(request.getRisk());
        }

        if (request.getStatus() != null) {
            Status status = statusService.findByName(request.getStatus());
            estimation.setStatus(status);
        }

        if (request.getCreator() != null) {
            estimation.setCreator(request.getCreator());
        }

        return estimation;
    }
}

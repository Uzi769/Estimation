package ru.irlix.evaluation.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.dao.mapper.PhaseMapper;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.repository.StatusRepository;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;
import ru.irlix.evaluation.service.EstimationService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private EstimationRepository estimationRepository;
    private StatusRepository statusRepository;
    private EstimationMapper estimationMapper;
    private PhaseMapper phaseMapper;

    @Override
    @Transactional
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = estimationMapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimation);

        log.info("Estimation with id " + savedEstimation.getId() + " saved");
        return estimationMapper.estimationToEstimationResponse(savedEstimation);
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
    public List<EstimationResponse> findAllEstimations(EstimationFilterRequest request) {
        List<Estimation> estimationList = estimationRepository.filter(request);
        log.info("Estimations filtered and found");
        return estimationMapper.estimationToEstimationResponse(estimationList);
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

    private Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));
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
            Status status = statusRepository.findById(request.getStatus())
                    .orElseThrow(() -> new NotFoundException("Status with id " + request.getStatus() + " not found"));
            estimation.setStatus(status);
        }

        if (request.getCreator() != null) {
            estimation.setCreator(request.getCreator());
        }
    }

    @Override
    public void unloadingEstimations() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Phases sheet");

        List<Estimation> estimations = estimationRepository.findAll();
        int rowNum = 0;

        //Header
        Row row = sheet.createRow(rowNum);
        setCell(row, "Id", 0);
        setCell(row, "Name", 1);
        setCell(row, "CreateDate", 2);
        setCell(row, "Risk", 3);
        setCell(row, "Status", 4);
        setCell(row, "Client", 5);
        setCell(row, "Creator", 6);

        //Data
        for (Estimation estimation : estimations) {
            rowNum++;
            row = sheet.createRow(rowNum);

            setCell(row, estimation.getId().toString(), 0);
            setCell(row, estimation.getName(), 1);
            setCell(row, estimation.getCreateDate().toString(), 2);
            setCell(row, estimation.getRisk().toString(), 3);
            setCell(row, estimation.getStatus().getDisplayValue(), 4);
            setCell(row, estimation.getClient(), 5);
            setCell(row, estimation.getCreator(), 6);
        }

        File file = new File("C:/output/estimations.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        log.info("unloading estimations into " + file.getAbsolutePath());
    }

    private void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
    }
}

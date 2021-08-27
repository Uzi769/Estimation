package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
        Phase phase = findPhaseById(id);
        checkAndUpdateFields(phase, phaseRequest);
        Phase savedPhase = phaseRepository.save(phase);

        log.info("Phase with id " + savedPhase.getId() + " updated");
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

    @Override
    public void unloadingPhases() throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Phases sheet");

        List<Phase> phases = phaseRepository.findAll();
        int rowNum = 0;
        //Header
        Row row = sheet.createRow(rowNum);
        setCell(row, "Id", 0);
        setCell(row, "Name", 1);
        setCell(row, "EstimationId", 2);
        setCell(row, "SortOrder", 3);
        setCell(row, "ManagementReserve", 4);
        setCell(row, "BagsReserve", 5);
        setCell(row, "QaReserve", 6);
        setCell(row, "RiskReserve", 7);
        setCell(row, "Done", 8);
        setCell(row, "BagsReserveOn", 9);
        setCell(row, "ManagementReserveOn", 10);
        setCell(row, "QaReserveOn", 11);
        setCell(row, "RiskReserveOn", 12);


        //Data
        for (Phase phase : phases) {
            rowNum++;
            row = sheet.createRow(rowNum);

            setCell(row, phase.getId().toString(), 0);
            setCell(row, phase.getName(), 1);
            setCell(row, phase.getEstimation().getId().toString(), 2);
            setCell(row, phase.getSortOrder().toString(), 3);
            setCell(row, phase.getManagementReserve().toString(), 4);
            setCell(row, phase.getBagsReserve().toString(), 5);
            setCell(row, phase.getQaReserve().toString(), 6);
            setCell(row, phase.getRiskReserve().toString(), 7);
            setCell(row, phase.getDone().toString(), 8);
            setCell(row, phase.getBagsReserveOn().toString(), 9);
            setCell(row, phase.getManagementReserveOn().toString(), 10);
            setCell(row, phase.getQaReserveOn().toString(), 11);
            setCell(row, phase.getRiskReserveOn().toString(), 12);
        }

        File file = new File("C:/output/phases.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        log.info("unloading phases into " + file.getAbsolutePath());
    }

    private void setCell(Row row, String name, Integer column) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellValue(name);
    }

}

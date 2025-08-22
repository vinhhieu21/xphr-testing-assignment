package org.example.xphrtestingassignment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.xphrtestingassignment.dto.ReportDTO;
import org.example.xphrtestingassignment.entity.TimeRecord;
import org.example.xphrtestingassignment.repository.TimeRecordRepository;
import org.example.xphrtestingassignment.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TimeRecordRepository timeRecordRepository;

    @Override
    public Page<ReportDTO> getReportData(LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         Pageable pageable) {
        // testing purpose
        //        return timeRecordRepository.findAll();
        return timeRecordRepository.findAllBetweenStartDateAndEndDate(startDate, endDate, pageable);
    }

    @Override
    public Page<ReportDTO> getReportDataForSpecificEmployee(
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        return timeRecordRepository.findAllByEmployeeAndDateRange(username, startDate, endDate, pageable);
    }
}

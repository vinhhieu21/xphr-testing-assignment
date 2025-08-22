package org.example.xphrtestingassignment.service.impl;

import org.example.xphrtestingassignment.dto.ReportDTO;
import org.example.xphrtestingassignment.repository.TimeRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private TimeRecordRepository timeRecordRepository;

    private LocalDateTime start;
    private LocalDateTime end;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().minusDays(30);
        end   = LocalDateTime.now();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getReportData_whenValidParams_thenDelegateAndReturnPage() {
        Page<ReportDTO> expected = new PageImpl<>(
                List.of(new ReportDTO("Tom", "Project A", BigDecimal.valueOf(12.5)))
        );
        when(timeRecordRepository.findAllBetweenStartDateAndEndDate(start, end, pageable))
                .thenReturn(expected);

        Page<ReportDTO> result = reportService.getReportData(start, end, pageable);

        assertThat(result).isSameAs(expected);
        verify(timeRecordRepository).findAllBetweenStartDateAndEndDate(start, end, pageable);
        verifyNoMoreInteractions(timeRecordRepository);
    }

    @Test
    void getReportData_whenRepositoryReturnsEmpty_thenReturnEmptyPage() {
        Page<ReportDTO> empty = Page.empty(pageable);
        when(timeRecordRepository.findAllBetweenStartDateAndEndDate(start, end, pageable))
                .thenReturn(empty);

        Page<ReportDTO> result = reportService.getReportData(start, end, pageable);

        assertThat(result.getTotalElements()).isZero();
        assertNotNull(result);
        verify(timeRecordRepository).findAllBetweenStartDateAndEndDate(start, end, pageable);
        verifyNoMoreInteractions(timeRecordRepository);
    }

    @Test
    void getReportDataForSpecificEmployee_whenValidParams_thenDelegateAndReturnPage() {
        String username = "jerry";
        Page<ReportDTO> expected = new PageImpl<>(
                List.of(new ReportDTO("Jerry", "Project B", BigDecimal.valueOf(8.0)))
        );
        when(timeRecordRepository.findAllByEmployeeAndDateRange(username, start, end, pageable))
                .thenReturn(expected);

        Page<ReportDTO> result =
                reportService.getReportDataForSpecificEmployee(username, start, end, pageable);

        assertThat(result).isSameAs(expected);
        verify(timeRecordRepository).findAllByEmployeeAndDateRange(username, start, end, pageable);
        verifyNoMoreInteractions(timeRecordRepository);
    }

    @Test
    void getReportDataForSpecificEmployee_whenRepositoryReturnsEmpty_thenReturnEmptyPage() {
        String username = "tom";
        Page<ReportDTO> empty = Page.empty(pageable);
        when(timeRecordRepository.findAllByEmployeeAndDateRange(username, start, end, pageable))
                .thenReturn(empty);

        Page<ReportDTO> result =
                reportService.getReportDataForSpecificEmployee(username, start, end, pageable);

        assertThat(result.getTotalElements()).isZero();
        assertNotNull(result);
        verify(timeRecordRepository).findAllByEmployeeAndDateRange(username, start, end, pageable);
        verifyNoMoreInteractions(timeRecordRepository);
    }
}
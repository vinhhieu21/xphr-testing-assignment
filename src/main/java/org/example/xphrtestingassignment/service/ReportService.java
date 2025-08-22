package org.example.xphrtestingassignment.service;

import org.example.xphrtestingassignment.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReportService {
    /**
     * Retrieves a paginated report of total hours worked by each employee on each project
     * within the specified date range.
     *
     * @param startDate the start date of the period to filter records
     * @param endDate   the end date of the period to filter records
     * @param pageable  pagination information
     * @return a page of ReportDTO containing employee names, project names, and total hours worked
     */
    Page<ReportDTO> getReportData(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Retrieves a paginated report of total hours worked by a specific user on each project
     * within the specified date range.
     *
     * @param username  the username of the employee to filter records
     * @param startDate the start date of the period to filter records
     * @param endDate   the end date of the period to filter records
     * @param pageable  pagination information
     * @return a page of ReportDTO containing employee names, project names, and total hours worked
     */
    Page<ReportDTO> getReportDataForSpecificEmployee(String username, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

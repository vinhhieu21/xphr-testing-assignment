package org.example.xphrtestingassignment.repository;


import org.example.xphrtestingassignment.dto.ReportDTO;
import org.example.xphrtestingassignment.entity.TimeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    @Query(value = "SELECT e.name AS employeeName, p.name AS projectName," +
            " SUM(EXTRACT(EPOCH FROM (tr.time_to - tr.time_from)) / 3600) AS totalHours " +
            "FROM time_record tr " +
            " JOIN employee e ON tr.employee_id = e.id " +
            " JOIN project p ON tr.project_id = p.id " +
            "WHERE tr.time_from >= :startDate " +
            "AND tr.time_to < :endDate " +
            "GROUP BY e.name, p.name " +
            "ORDER BY e.name, p.name", nativeQuery = true)
    Page<ReportDTO> findAllBetweenStartDateAndEndDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query(value = "SELECT e.name AS employeeName, p.name AS projectName," +
            " SUM(EXTRACT(EPOCH FROM (tr.time_to - tr.time_from)) / 3600) AS totalHours " +
            "FROM time_record tr " +
            " JOIN employee e ON tr.employee_id = e.id " +
            " JOIN project p ON tr.project_id = p.id " +
            "WHERE tr.time_from >= :startDate " +
            "AND tr.time_to < :endDate " +
            "AND e.name = :username " +
            "GROUP BY e.name, p.name " +
            "ORDER BY e.name, p.name", nativeQuery = true)
    Page<ReportDTO> findAllByEmployeeAndDateRange(
            @Param("username") String username,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}

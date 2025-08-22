package org.example.xphrtestingassignment.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.xphrtestingassignment.annotation.UserAuthentication;
import org.example.xphrtestingassignment.constant.UserRoles;
import org.example.xphrtestingassignment.dto.ReportDTO;
import org.example.xphrtestingassignment.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @UserAuthentication
    @GetMapping("/reports")
    public String getReportData(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);

        var username = userDetails.getUsername();
        log.info("user: {} logged in at {}", username, LocalDateTime.now());
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(auth ->
                auth.getAuthority().equalsIgnoreCase("ROLE_" + UserRoles.ADMIN.name()));

        // we have 2 ways to get report data here:
        // 1. Admin can see all data
        // 2. Employee can only see their own data -> so we can filter it in code
        // but it's better to use query with username = employee_name -> avoid get all data.
        // assuming that username is employee_name
        Page<ReportDTO> reportData;
        if (isAdmin) {
            reportData = reportService.getReportData(startDate, endDate, pageable);
        } else {
            reportData = reportService.getReportDataForSpecificEmployee(username, startDate, endDate, pageable);
        }

        model.addAttribute("reportData", reportData);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        model.addAttribute("username", username);
        model.addAttribute("role", (isAdmin ? UserRoles.ADMIN.name() : UserRoles.EMPLOYEE.name()));

        log.info("user: {} get report data from {} to {}, page: {}, size: {}, data {}",
                username, startDate, endDate, page, size, reportData.getContent());

        return "work_hours_report";
    }
}
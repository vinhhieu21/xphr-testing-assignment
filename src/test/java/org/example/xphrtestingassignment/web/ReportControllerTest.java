package org.example.xphrtestingassignment.web;

import org.example.xphrtestingassignment.constant.UserRoles;
import org.example.xphrtestingassignment.dto.ReportDTO;
import org.example.xphrtestingassignment.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController controller;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

    @Test
    void getReportData_whenAdmin_thenCallsAdminServiceAndReturnsViewWithModel() {
        int pageIdx = 2;
        int size = 20;
        LocalDateTime start = null;
        LocalDateTime end   = null;

        UserDetails admin = User.withUsername("admin")
                .password("x")
                .roles(UserRoles.ADMIN.name())
                .build();

        Page<ReportDTO> expected = new PageImpl<>(
                List.of(new ReportDTO("Admin", "Project X", BigDecimal.valueOf(15.0)))
        );
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(reportService.getReportData(eq(start), eq(end), any(Pageable.class)))
                .thenReturn(expected);

        String view = controller.getReportData(start, end, pageIdx, size, admin, model);

        assertThat(view).isEqualTo("work_hours_report");

        verify(reportService).getReportData(eq(start), eq(end), pageableCaptor.capture());
        verifyNoMoreInteractions(reportService);

        Pageable usedPageable = pageableCaptor.getValue();
        assertThat(usedPageable.getPageNumber()).isEqualTo(pageIdx);
        assertThat(usedPageable.getPageSize()).isEqualTo(size);

        assertThat(model.getAttribute("reportData")).isSameAs(expected);
        assertThat(model.getAttribute("startDate")).isNull();
        assertThat(model.getAttribute("endDate")).isNull();
        assertThat(model.getAttribute("currentPage")).isEqualTo(pageIdx);
        assertThat(model.getAttribute("pageSize")).isEqualTo(size);
        assertThat(model.getAttribute("username")).isEqualTo("admin");
        assertThat(model.getAttribute("role")).isEqualTo(UserRoles.ADMIN.name());
    }

    @Test
    void getReportData_whenEmployee_thenCallsEmployeeServiceAndReturnsViewWithModel() {
        int pageIdx = 0;
        int size = 10;
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end   = LocalDateTime.now();

        UserDetails employee = User.withUsername("tom")
                .password("x")
                .roles(UserRoles.EMPLOYEE.name())
                .build();

        Page<ReportDTO> expected = new PageImpl<>(
                List.of(new ReportDTO("tom", "Project A", BigDecimal.valueOf(12.6)))
        );
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(reportService.getReportDataForSpecificEmployee(eq("tom"), eq(start), eq(end), any(Pageable.class)))
                .thenReturn(expected);

        String view = controller.getReportData(start, end, pageIdx, size, employee, model);

        assertThat(view).isEqualTo("work_hours_report");

        verify(reportService).getReportDataForSpecificEmployee(eq("tom"), eq(start), eq(end), pageableCaptor.capture());
        verifyNoMoreInteractions(reportService);

        Pageable usedPageable = pageableCaptor.getValue();
        assertThat(usedPageable.getPageNumber()).isEqualTo(pageIdx);
        assertThat(usedPageable.getPageSize()).isEqualTo(size);

        assertThat(model.getAttribute("reportData")).isSameAs(expected);
        assertThat(model.getAttribute("startDate")).isEqualTo(start);
        assertThat(model.getAttribute("endDate")).isEqualTo(end);
        assertThat(model.getAttribute("currentPage")).isEqualTo(pageIdx);
        assertThat(model.getAttribute("pageSize")).isEqualTo(size);
        assertThat(model.getAttribute("username")).isEqualTo("tom");
        assertThat(model.getAttribute("role")).isEqualTo(UserRoles.EMPLOYEE.name());
    }

    @Test
    void getReportData_whenNullDates_thenPassNullsThroughToService() {
        UserDetails admin = User.withUsername("admin")
                .password("x")
                .roles(UserRoles.ADMIN.name())
                .build();

        Page<ReportDTO> expected = Page.empty();
        when(reportService.getReportData(isNull(), isNull(), any(Pageable.class)))
                .thenReturn(expected);

        String view = controller.getReportData(null, null, 0, 10, admin, model);

        assertThat(view).isEqualTo("work_hours_report");
        verify(reportService).getReportData(isNull(), isNull(), any(Pageable.class));
        assertThat(model.getAttribute("startDate")).isNull();
        assertThat(model.getAttribute("endDate")).isNull();
    }

    @Test
    void getReportData_whenPagingProvided_thenPageAndSizeAddedToModel() {
        int pageIdx = 3;
        int size = 15;

        UserDetails admin = User.withUsername("admin")
                .password("x")
                .roles(UserRoles.ADMIN.name())
                .build();

        Page<ReportDTO> expected = Page.empty();
        when(reportService.getReportData(any(), any(), any(Pageable.class))).thenReturn(expected);

        controller.getReportData(null, null, pageIdx, size, admin, model);

        assertThat(model.getAttribute("currentPage")).isEqualTo(pageIdx);
        assertThat(model.getAttribute("pageSize")).isEqualTo(size);
    }
}
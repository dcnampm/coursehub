package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
public interface WeeklyReportService {
    List<WeeklyReportDto> getAllReports();

    WeeklyReportDto getReportById(Long id);

    void saveReport(WeeklyReportDto weeklyReportDto);

    void deleteReport(Long id);
}

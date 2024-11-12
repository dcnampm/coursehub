package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.entity.WeeklyReport;
import org.springframework.stereotype.Component;

@Component
public class WeeklyReportMapper {
    public WeeklyReportDto toWeeklyReportDto(WeeklyReport weeklyReport) {
        return new WeeklyReportDto(
                weeklyReport.getId(),
                weeklyReport.getCreatedBy(),
                weeklyReport.getCreatedAt(),
                weeklyReport.getReportStartDate(),
                weeklyReport.getReportEndDate(),
                weeklyReport.getFilePath()
        );
    }

    public WeeklyReport toWeeklyReport(WeeklyReportDto weeklyReportDto) {
        WeeklyReport report = new WeeklyReport();
        report.setId(weeklyReportDto.getId());
        report.setCreatedBy(weeklyReportDto.getCreatedBy());
        report.setCreatedAt(weeklyReportDto.getCreatedAt());
        report.setReportStartDate(weeklyReportDto.getReportStartDate());
        report.setReportEndDate(weeklyReportDto.getReportEndDate());
        report.setFilePath(weeklyReportDto.getFilePath());
        return report;
    }
}

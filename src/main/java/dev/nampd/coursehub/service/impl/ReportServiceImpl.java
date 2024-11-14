package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.WeeklyReportMapper;
import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.entity.WeeklyReport;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.repository.ReportRepository;
import dev.nampd.coursehub.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final WeeklyReportMapper reportMapper;

    public ReportServiceImpl(ReportRepository reportRepository, WeeklyReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public PagedResponse<WeeklyReportDto> getWeeklyReportsPaginated(int page, int size) {
        Page<WeeklyReport> reportPage = reportRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

        List<WeeklyReportDto> reportDtoList = reportPage
                .map(reportMapper::toWeeklyReportDto)
                .getContent();

        return new PagedResponse<>(
                reportDtoList,
                reportPage.getNumber(),
                reportPage.getSize(),
                reportPage.getTotalElements(),
                reportPage.getTotalPages()
        );
    }

    @Override
    public List<WeeklyReportDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toWeeklyReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyReportDto getReportById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toWeeklyReportDto)
                .orElse(null);
    }

    @Override
    public void saveReport(WeeklyReportDto weeklyReportDto) {
        WeeklyReport report = reportMapper.toWeeklyReport(weeklyReportDto);
        reportRepository.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}

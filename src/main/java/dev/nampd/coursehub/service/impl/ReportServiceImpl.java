package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.WeeklyReportMapper;
import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.entity.WeeklyReport;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.repository.WeeklyReportRepository;
import dev.nampd.coursehub.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private final WeeklyReportRepository weeklyReportRepository;
    private final WeeklyReportMapper reportMapper;

    public ReportServiceImpl(WeeklyReportRepository weeklyReportRepository, WeeklyReportMapper reportMapper) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public PagedResponse<WeeklyReportDto> getWeeklyReportsPaginated(int page, int size) {
        Page<WeeklyReport> reportPage = weeklyReportRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

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
        return weeklyReportRepository.findAll().stream()
                .map(reportMapper::toWeeklyReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyReportDto getReportById(Long id) {
        return weeklyReportRepository.findById(id)
                .map(reportMapper::toWeeklyReportDto)
                .orElse(null);
    }

    @Override
    public void saveReport(WeeklyReportDto weeklyReportDto) {
        WeeklyReport report = reportMapper.toWeeklyReport(weeklyReportDto);
        weeklyReportRepository.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        weeklyReportRepository.deleteById(id);
    }
}

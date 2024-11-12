package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.WeeklyReportMapper;
import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.entity.WeeklyReport;
import dev.nampd.coursehub.repository.WeeklyReportRepository;
import dev.nampd.coursehub.service.WeeklyReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyReportServiceImpl implements WeeklyReportService {
    private final WeeklyReportRepository weeklyReportRepository;
    private final WeeklyReportMapper weeklyReportMapper;

    public WeeklyReportServiceImpl(WeeklyReportRepository weeklyReportRepository, WeeklyReportMapper weeklyReportMapper) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.weeklyReportMapper = weeklyReportMapper;
    }

    @Override
    public List<WeeklyReportDto> getAllReports() {
        return weeklyReportRepository.findAll().stream()
                .map(weeklyReportMapper::toWeeklyReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyReportDto getReportById(Long id) {
        return weeklyReportRepository.findById(id)
                .map(weeklyReportMapper::toWeeklyReportDto)
                .orElse(null);
    }

    @Override
    public void saveReport(WeeklyReportDto weeklyReportDto) {
        WeeklyReport report = weeklyReportMapper.toWeeklyReport(weeklyReportDto);
        weeklyReportRepository.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        weeklyReportRepository.deleteById(id);
    }
}

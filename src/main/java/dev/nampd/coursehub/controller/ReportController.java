package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.entity.WeeklyReport;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.repository.ReportRepository;
import dev.nampd.coursehub.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @GetMapping()
    public String getWeeklyReports(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "12") int size,
                                   Model model) {
        PagedResponse<WeeklyReportDto> reportPagedResponse =  reportService.getWeeklyReportsPaginated(page, size);
        model.addAttribute("weeklyReports", reportPagedResponse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reportPagedResponse.getTotalPages());
        model.addAttribute("pageSize", size);
        return "report/list";
    }

    @GetMapping("/download-report/{id}")
    public ResponseEntity<FileSystemResource> downloadReport(@PathVariable Long id) {
        Optional<WeeklyReport> reportOpt = reportRepository.findById(id);
        if (reportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        WeeklyReport report = reportOpt.get();
        File file = new File(report.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}

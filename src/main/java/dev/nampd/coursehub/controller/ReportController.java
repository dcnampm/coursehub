package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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

@Controller
@RequestMapping("/admin/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/weekly-reports")
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
}

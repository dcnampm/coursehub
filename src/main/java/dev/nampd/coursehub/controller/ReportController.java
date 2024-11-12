package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.WeeklyReportDto;
import dev.nampd.coursehub.service.WeeklyReportService;
import org.springframework.core.io.InputStreamResource;
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
    private final WeeklyReportService weeklyReportService;

    public ReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @GetMapping
    public String getAllReports(Model model) {
        model.addAttribute("reports", weeklyReportService.getAllReports());
        return "/report/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("weeklyReportDto", new WeeklyReportDto());
        return "/report/add-form";
    }

    @PostMapping("/add")
    public String addReport(@ModelAttribute WeeklyReportDto weeklyReportDto,
                            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "uploads/reports/";

            try {
                // Tạo thư mục nếu chưa tồn tại
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Lưu file vào thư mục
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);

                // Cập nhật đường dẫn file vào DTO
                weeklyReportDto.setFilePath(dest.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần
            }
        }

        // Lưu báo cáo vào cơ sở dữ liệu
        weeklyReportService.saveReport(weeklyReportDto);
        return "redirect:/admin/reports";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id,
                                 Model model) {
        WeeklyReportDto weeklyReportDto = weeklyReportService.getReportById(id);
        model.addAttribute("weeklyReportDto", weeklyReportDto);
        return "report/update-form";
    }

    @PostMapping("/edit/{id}")
    public String editReport(@PathVariable Long id,
                             @ModelAttribute WeeklyReportDto weeklyReportDto,
                             @RequestParam("file") MultipartFile file) {
        WeeklyReportDto existingReport = weeklyReportService.getReportById(id);
        if (existingReport == null) {
            return "redirect:/admin/reports";
        }

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "uploads/reports/";

            try {
                // Tạo thư mục nếu chưa tồn tại
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Lưu file vào thư mục
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);

                // Cập nhật đường dẫn file vào DTO
                weeklyReportDto.setFilePath(dest.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần
            }
        } else {
            // Nếu không cập nhật file, giữ nguyên đường dẫn file hiện tại
            weeklyReportDto.setFilePath(existingReport.getFilePath());
        }

        // Cập nhật ID để đảm bảo cập nhật đúng đối tượng
        weeklyReportDto.setId(id);

        // Lưu báo cáo vào cơ sở dữ liệu
        weeklyReportService.saveReport(weeklyReportDto);
        return "redirect:/admin/reports";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        weeklyReportService.deleteReport(id);
        return "redirect:/reports";
    }

    // Xử lý tải xuống file báo cáo CSV/PDF
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable Long id) throws IOException {
        WeeklyReportDto report = weeklyReportService.getReportById(id);
        if (report == null || report.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(report.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Xác định loại file dựa trên phần mở rộng
        String fileName = file.getName();
        MediaType mediaType;
        if (fileName.endsWith(".csv")) {
            mediaType = MediaType.parseMediaType("text/csv");
        } else if (fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .body(resource);
    }
}

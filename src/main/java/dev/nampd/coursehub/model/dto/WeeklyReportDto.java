package dev.nampd.coursehub.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReportDto {
    private Long id;
    private String title;
    private String createdBy;
    private LocalDate createdAt;
    private LocalDate reportStartDate;
    private LocalDate reportEndDate;
    private String filePath;
}

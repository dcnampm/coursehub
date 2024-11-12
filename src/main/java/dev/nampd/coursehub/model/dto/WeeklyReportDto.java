package dev.nampd.coursehub.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReportDto {
    private Long id;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

    private String filePath;
}

package dev.nampd.coursehub.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weekly_reports")
@Getter
@Setter
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String createdBy;

    private LocalDate createdAt;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

    private String filePath;

    public WeeklyReport() {}

    public WeeklyReport(String createdBy, LocalDate createdAt, LocalDate reportStartDate, LocalDate reportEndDate, String filePath) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.reportStartDate = reportStartDate;
        this.reportEndDate = reportEndDate;
        this.filePath = filePath;
    }
}


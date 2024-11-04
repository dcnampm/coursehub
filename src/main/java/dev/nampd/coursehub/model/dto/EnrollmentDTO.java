package dev.nampd.coursehub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private LocalDateTime enrollmentDate;
    private boolean isActive;
}

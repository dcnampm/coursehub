package dev.nampd.coursehub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private int maxSize;
    private boolean isFull;
    private List<EnrollmentDTO> enrollments;
}

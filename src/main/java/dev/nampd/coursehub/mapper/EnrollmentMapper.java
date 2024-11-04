package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.EnrollmentDTO;
import dev.nampd.coursehub.model.entity.Enrollment;

public class EnrollmentMapper {
    public EnrollmentDTO toEnrollmentDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getEnrollmentDate(),
                enrollment.isActive()
        );
    }
}

package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.EnrollmentDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface EnrollmentService {
    void enrollInCourse(Long studentId, Long courseId);

    void cancelEnrollment(Long studentId, Long courseId);

//    void deleteEnrollment(Long studentId, Long courseId);

    List<EnrollmentDto> getEnrollmentsForStudent(Long studentId);

    List<EnrollmentDto> getEnrollmentsForCourse(Long courseId);
}

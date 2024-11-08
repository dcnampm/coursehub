package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDto enrollInCourse(Long studentId, Long courseId);

    void cancelEnrollment(Long enrollmentId);

    List<EnrollmentDto> getEnrollmentsForStudent(Long studentId);

    List<EnrollmentDto> getEnrollmentsForCourse(Long courseId);
}

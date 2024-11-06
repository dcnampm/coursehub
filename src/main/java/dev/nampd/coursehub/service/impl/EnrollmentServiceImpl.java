package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.EnrollmentMapper;
import dev.nampd.coursehub.model.dto.EnrollmentDto;
import dev.nampd.coursehub.model.entity.Course;
import dev.nampd.coursehub.model.entity.Enrollment;
import dev.nampd.coursehub.model.entity.Student;
import dev.nampd.coursehub.repository.CourseRepository;
import dev.nampd.coursehub.repository.EnrollmentRepository;
import dev.nampd.coursehub.repository.StudentRepository;
import dev.nampd.coursehub.service.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmentDto enroll(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (course.isFull()) {
            throw new IllegalStateException("Course is already full");
        }

        Enrollment enrollment = new Enrollment(student, course);
        course.getEnrollments().add(enrollment);
        course.decrementRemainingSlots();

        courseRepository.save(course);
        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toEnrollmentDto(enrollment);
    }

    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        enrollment.setActive(false);
        Course course = enrollment.getCourse();
        course.getEnrollments().remove(enrollment);
        course.incrementRemainingSlots();
        enrollment.getCourse().updateStatus();

        courseRepository.save(course);
        enrollmentRepository.save(enrollment);
    }

    public List<EnrollmentDto> getEnrollmentsForStudent(Long studentId) {
        return enrollmentRepository.findByStudentIdAndIsActiveTrue(studentId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getEnrollmentsForCourse(Long courseId) {
        return enrollmentRepository.findByCourseIdAndIsActiveTrue(courseId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }
}

package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.EnrollmentMapper;
import dev.nampd.coursehub.model.dto.CourseDto;
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

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
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

    @Override
    public EnrollmentDto enrollInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (enrollmentRepository.findByStudentIdAndCourseIdAndIsActiveTrue(studentId, courseId).isPresent()) {
            throw new IllegalStateException("Sinh viên đã đăng ký khóa học này!");
        }

        if (course.isFull()) {
            throw new IllegalStateException("Course is already full");
        }

        Enrollment enrollment = new Enrollment(student, course);
        course.getEnrollments().add(enrollment);

        course.updateStatus();
        courseRepository.save(course);
        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toEnrollmentDto(enrollment);
    }

    @Override
    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        Course course = enrollment.getCourse();
        LocalDateTime now = LocalDateTime.now();

//        if (!now.toLocalDate().isBefore(course.getStartDate())) {
//            throw new IllegalStateException("Không thể hủy đăng ký sau khi khóa học đã bắt đầu.");
//        }

        if (course.getStartDate().isBefore(ChronoLocalDate.from(now)) || course.getStartDate().isEqual(ChronoLocalDate.from(now))) {
            throw new IllegalStateException("Không thể hủy đăng ký sau khi khóa học đã bắt đầu.");
        }

        enrollment.setActive(false);
        course.getEnrollments().remove(enrollment);
        course.updateStatus();
        enrollment.getCourse().updateStatus();

        courseRepository.save(course);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForStudent(Long studentId) {
        return enrollmentRepository.findByStudentIdAndIsActiveTrue(studentId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForCourse(Long courseId) {
        return enrollmentRepository.findByCourseIdAndIsActiveTrue(courseId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }
}
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void enrollInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        LocalDateTime now = LocalDateTime.now();

        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new IllegalStateException("Sinh viên đã đăng ký khóa học này!");
        }

        if (course.isFull()) {
            throw new IllegalStateException("Khóa học đã đầy");
        }

        if (!now.toLocalDate().isBefore(course.getStartDate())) {
            throw new IllegalStateException("Khóa học đã bắt đầu");
        }

        Enrollment enrollment = new Enrollment(student, course);
        course.getEnrollments().add(enrollment);
        course.updateStatus();

        courseRepository.save(course);
    }

    @Transactional
    @Override
    public void cancelEnrollment(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        LocalDateTime now = LocalDateTime.now();

        if (!now.toLocalDate().isBefore(course.getStartDate())) {
            throw new IllegalStateException("Không thể hủy đăng ký sau khi khóa học đã bắt đầu.");
        }

        System.out.println("Before_course" + course.getEnrollments());
        System.out.println("Before_student" + student.getEnrollments());
        course.getEnrollments().remove(enrollment);
        course.updateStatus();
        student.getEnrollments().remove(enrollment);

        courseRepository.save(course);
        studentRepository.save(student);
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        System.out.println("After_course" + course.getEnrollments());
        System.out.println("After_stu" + student.getEnrollments());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());
    }
}
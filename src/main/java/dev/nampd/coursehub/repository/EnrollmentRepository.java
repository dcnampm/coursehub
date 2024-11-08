package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.entity.Course;
import dev.nampd.coursehub.model.entity.Enrollment;
import dev.nampd.coursehub.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndIsActiveTrue(Long studentId);
    List<Enrollment> findByCourseIdAndIsActiveTrue(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseIdAndIsActiveTrue(Long studentId, Long courseId);
}

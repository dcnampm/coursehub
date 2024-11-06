package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentIdAndIsActiveTrue(Long studentId);
    List<Enrollment> findByCourseIdAndIsActiveTrue(Long courseId);
}

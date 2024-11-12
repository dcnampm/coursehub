package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s WHERE NOT EXISTS (SELECT e FROM Enrollment e WHERE e.student.id = s.id)")
    List<StudentDto> findStudentsWithNoEnrollments();

    @Query("SELECT s FROM Student s WHERE NOT EXISTS (SELECT e FROM Enrollment e WHERE e.student.id = s.id AND e.course.id = :courseId)")
    List<StudentDto> findStudentsNotEnrolledInCourse(@Param("courseId") Long courseId);
}

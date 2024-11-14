package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.DetailedReportDto;
import dev.nampd.coursehub.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Course c LEFT JOIN Enrollment e ON c.id = e.course.id AND e.student.id = :studentId " +
            "WHERE c.isFull = false AND c.startDate > :currentDate AND e.id IS NULL")
    List<Course> findCoursesForStudentReminder(@Param("studentId") Long studentId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT new dev.nampd.coursehub.model.dto.DetailedReportDto(" +
            "c.name, " +
            "CASE WHEN c.isFull = true THEN 'Full' ELSE 'Open' END, " +
            "COUNT(e.id)) " +
            "FROM Course c LEFT JOIN c.enrollments e ON e.enrollmentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY c.id, c.name, c.isFull")
    List<DetailedReportDto> findCourseReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

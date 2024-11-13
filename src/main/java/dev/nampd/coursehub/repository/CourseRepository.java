package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.dto.CourseDto;
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

    @Query("SELECT c FROM Course c WHERE c.isFull = false AND c.startDate > :currentDate AND c.id NOT IN " +
            "(SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId)")
    List<Course> findCoursesForStudentReminder(@Param("studentId") Long studentId, @Param("currentDate") LocalDate currentDate);

}

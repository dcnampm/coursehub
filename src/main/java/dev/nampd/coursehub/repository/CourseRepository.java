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

    @Query("SELECT c FROM Course c WHERE c.startDate = :date AND c.maxSize > (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id)")
    List<CourseDto> findCoursesWithVacanciesStartingOn(LocalDate date);

}

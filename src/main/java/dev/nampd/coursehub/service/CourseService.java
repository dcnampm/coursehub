package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CourseService {
    @PreAuthorize("hasRole('ADMIN')")
    void createCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
    @PreAuthorize("hasRole('ADMIN')")
    void updateCourse(Long id, CourseDto courseDto);
    @PreAuthorize("hasRole('ADMIN')")
    void deleteCourse(Long id);
    PagedResponse<CourseDto> getCoursesPaginated(int page, int size);
}

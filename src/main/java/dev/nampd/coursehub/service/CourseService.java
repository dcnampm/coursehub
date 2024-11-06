package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.response.PagedResponse;

import java.util.List;

public interface CourseService {
    void createCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
    void updateCourse(Long id, CourseDto courseDto);
    void deleteCourse(Long id);

    PagedResponse<CourseDto> getCoursesPaginated(int page, int size);
}

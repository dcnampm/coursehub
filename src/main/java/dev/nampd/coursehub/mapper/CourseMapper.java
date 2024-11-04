package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.CourseDTO;
import dev.nampd.coursehub.model.dto.EnrollmentDTO;
import dev.nampd.coursehub.model.entity.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {
    private final EnrollmentMapper enrollmentMapper;

    public CourseMapper(EnrollmentMapper enrollmentMapper) {
        this.enrollmentMapper = enrollmentMapper;
    }
    public CourseDTO toCourseDTO(Course course) {
        List<EnrollmentDTO> enrollments = course.getEnrollments().stream()
                .map(enrollmentMapper::toEnrollmentDTO)
                .collect(Collectors.toList());

        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getMaxSize(),
                course.isFull(),
                enrollments
        );
    }

    public Course toCourse(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }
        Course course = new Course();

        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setMaxSize(courseDTO.getMaxSize());
        course.setFull(courseDTO.isFull());

        return course;
    }
}

package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.EnrollmentDto;
import dev.nampd.coursehub.model.entity.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {
    private final EnrollmentMapper enrollmentMapper;

    public CourseMapper(EnrollmentMapper enrollmentMapper) {
        this.enrollmentMapper = enrollmentMapper;
    }
    public CourseDto toCourseDto(Course course) {
        List<EnrollmentDto> enrollments = course.getEnrollments().stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .collect(Collectors.toList());

        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getStartDate(),
                course.getEndDate(),
                course.getMaxSize(),
                course.isFull(),
                enrollments
        );
    }

    public Course toCourse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
        Course course = new Course();

        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setMaxSize(courseDto.getMaxSize());
        course.setFull(courseDto.isFull());

        return course;
    }
}

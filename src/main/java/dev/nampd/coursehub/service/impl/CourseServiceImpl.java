package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.CourseMapper;
import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.entity.Course;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.repository.CourseRepository;
import dev.nampd.coursehub.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public void createCourse(CourseDto courseDto) {
        Course course = courseMapper.toCourse(courseDto);

        if (courseRepository.existsByName(course.getName())) {
            throw new IllegalArgumentException("Course already exists");
        }

        courseRepository.save(course);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toCourseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseMapper.toCourseDto(course);
    }

    @Override
    public void updateCourse(Long id, CourseDto updatedCourseDto) {
        Course existingCourse = courseRepository.findById(updatedCourseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        existingCourse.setName(updatedCourseDto.getName());
        existingCourse.setDescription(updatedCourseDto.getDescription());
        existingCourse.setMaxSize(updatedCourseDto.getMaxSize());
        existingCourse.setFull(updatedCourseDto.isFull());
        existingCourse.setStartDate(updatedCourseDto.getStartDate());
        existingCourse.setEndDate(updatedCourseDto.getEndDate());

        courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found");
        }

        courseRepository.deleteById(id);
    }

    @Override
    public PagedResponse<CourseDto> getCoursesPaginated(int page, int size) {
        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

        List<CourseDto> courseDtoList = coursePage
                .map(courseMapper::toCourseDto)
                .getContent();

        return new PagedResponse<>(
                courseDtoList,
                coursePage.getNumber(),
                coursePage.getSize(),
                coursePage.getTotalElements(),
                coursePage.getTotalPages()
        );
    }
}

package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.EnrollmentDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.service.CourseService;
import dev.nampd.coursehub.service.EnrollmentService;
import dev.nampd.coursehub.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, StudentService studentService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String getAllCourses(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "6") int size,
                                 Model model) {
        PagedResponse<CourseDto> coursePagedResponse = courseService.getCoursesPaginated(page, size);
        model.addAttribute("courses", coursePagedResponse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePagedResponse.getTotalPages());
        model.addAttribute("pageSize", size);
        return "course/list";
    }

    @GetMapping("/detail/{id}")
    public String getCourseDetail(@PathVariable Long id, Model model) {
        CourseDto courseDto = courseService.getCourseById(id);
        List<EnrollmentDto> enrollmentsForCourse = enrollmentService.getEnrollmentsForCourse(id);

        List<StudentDto> enrolledStudents = enrollmentsForCourse.stream()
                .map(enrollment -> studentService.getStudentById(enrollment.getStudentId()))
                .collect(Collectors.toList());

        model.addAttribute("enrollments", enrollmentsForCourse);
        model.addAttribute("course", courseDto);
        model.addAttribute("students", enrolledStudents);
        return "course/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new CourseDto());
        return "course/add-form";
    }

    @PostMapping
    public String createCourse(@Valid @ModelAttribute("course") CourseDto courseDto) {
        courseService.createCourse(courseDto);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        CourseDto courseDto = courseService.getCourseById(id);
        model.addAttribute("course", courseDto);
        return "course/update-form";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id,
                                @ModelAttribute("course") CourseDto courseDto) {
        courseService.updateCourse(id, courseDto);
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}

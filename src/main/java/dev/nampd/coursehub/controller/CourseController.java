package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getAllCourses(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        PagedResponse<CourseDto> coursePagedResponse = courseService.getCoursesPaginated(page, size);
        model.addAttribute("courses", coursePagedResponse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePagedResponse.getTotalPages());
        model.addAttribute("pageSize", size);
        return "course/list";
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

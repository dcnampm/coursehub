package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getAllStudents(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        PagedResponse<StudentDto> studentPagedResponse = studentService.getStudentsPaginated(page, size);
        model.addAttribute("students", studentPagedResponse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPagedResponse.getTotalPages());
        model.addAttribute("pageSize", size);
        return "student/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "student/add-form";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") StudentDto studentDto) {
        studentService.createStudent(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        StudentDto studentDto = studentService.getStudentById(id);
        model.addAttribute("student", studentDto);
        return "student/update-form";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") StudentDto studentDto) {
        studentService.updateStudent(id, studentDto);
        return "redirect:/students";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}

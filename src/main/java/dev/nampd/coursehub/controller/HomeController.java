package dev.nampd.coursehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/admin/home")
    public String showAdminHome() {
        return "admin-home";
    }

    @GetMapping("/student/home")
    public String showStudentHome() {
        return "student-home";
    }
}

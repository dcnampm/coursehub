package dev.nampd.coursehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showIntroPage() {
        return "intro";
    }

    @GetMapping("/admin/home")
    public String showAdminHome() {
        return "home/admin-home";
    }

    @GetMapping("/student/home")
    public String showStudentHome() {
        return "home/student-home";
    }
}

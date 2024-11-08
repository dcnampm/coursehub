package dev.nampd.coursehub.controller;

import dev.nampd.coursehub.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public String enrollInCourse(@RequestParam Long studentId,
                                 @RequestParam Long courseId,
                                 RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.enrollInCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/courses/detail/" + courseId;
    }

    @PostMapping("/cancel/{enrollmentId}")
    public String cancelEnrollment(@PathVariable Long enrollmentId,
                                   RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.cancelEnrollment(enrollmentId);
            redirectAttributes.addFlashAttribute("message", "Hủy đăng ký thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses";
    }

}

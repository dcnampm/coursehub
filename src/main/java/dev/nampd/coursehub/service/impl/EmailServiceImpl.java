package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Student;
import dev.nampd.coursehub.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;

    // Gửi email nhắc nhở sinh viên chưa đăng ký khóa học
    @Override
    public void sendStudentReminderEmail(StudentDto student) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(student.getEmail());
        message.setSubject("Nhắc nhở Đăng ký Khóa học");
        message.setText("Dear " + student.getFullName() + ",\n\n" +
                "We noticed that you haven’t enrolled in any courses yet. At VietIS Education, we are dedicated to helping you achieve your academic and career goals. Enrolling in a course is a great way to expand your skills and knowledge!\n\n" +
                "Take a moment to browse our available courses and find the ones that suit your interests and career goals. We are here to support you on your journey to success!\n\n" +
                "If you need any assistance with enrollment, please don't hesitate to reach out to us.\n\n" +
                "Warm regards,\n" +
                "VietIS Education Support Team\n\n" +
                "Contact us at: nampd@vietis.com.vn\n" +
                "Visit us: https://www.vietis.edu.vn/");
        mailSender.send(message);
    }

    // Gửi email thông báo khóa học còn chỗ và sắp hết hạn đăng ký
    @Override
    public void sendCourseExpirationEmail(String courseName, String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Thông báo khóa học sắp hết hạn đăng ký");
        message.setText("Dear all student,\n\n" +
                "We hope you are enjoying your learning experience with VietIS Education!\n\n" +
                "We wanted to let you know that our upcoming course, \"" + courseName + "\", is starting tomorrow, and we still have spots available. This is a unique opportunity to enhance your skills and knowledge in a dynamic learning environment, led by experienced instructors.\n\n" +
                "Take a moment to review the course details on our platform. If this aligns with your goals, we encourage you to secure your spot by enrolling today!\n\n" +
                "Should you have any questions or need assistance with enrollment, please don’t hesitate to reach out to us.\n\n" +
                "Best wishes,\n" +
                "VietIS Education Support Team\n\n" +
                "Contact us at: nampd@vietis.com.vn\n" +
                "Visit us: https://www.vietis.edu.vn/");
        mailSender.send(message);
    }
}

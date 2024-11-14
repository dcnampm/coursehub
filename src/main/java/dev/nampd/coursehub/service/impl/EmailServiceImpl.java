package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Gửi email nhắc nhở sinh viên chưa đăng ký khóa học
    @Override
    public void sendStudentReminderEmail(StudentDto student, List<CourseDto> courses) {
        String emailContent = buildEmailContent(student, courses);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(student.getEmail());
        message.setSubject("Reminder: Course Enrollment");
        message.setText(emailContent);
        mailSender.send(message);
    }

    private String buildEmailContent(StudentDto student, List<CourseDto> courses) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(student.getFullName()).append(",\n\n");
        content.append("We noticed that you haven’t enrolled in some courses that may be beneficial to you. Here is a list of available courses you may want to consider:\n\n");

        for (CourseDto course : courses) {
            content.append("- ").append(course.getName()).append("\n");
            content.append("  Description: ").append(course.getDescription()).append("\n");
            content.append("  Start Date: ").append(course.getStartDate()).append("\n");
            content.append("  End Date: ").append(course.getEndDate()).append("\n\n");
        }

        content.append("Please log in to our system to enroll in these courses and take advantage of the opportunities to expand your knowledge and skills.\n\n");
        content.append("Best regards,\n");
        content.append("VietIS Education Support Team\n");
        content.append("Contact Support: support@vietisedu.com.vn\n");

        return content.toString();
    }
}

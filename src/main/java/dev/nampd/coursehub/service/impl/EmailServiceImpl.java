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
    private JavaMailSender mailSender;

    // Gửi email nhắc nhở sinh viên chưa đăng ký khóa học
    @Override
    public void sendStudentReminderEmail(StudentDto student, List<CourseDto> courses) {
        String emailContent = buildEmailContent(student, courses);

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

    private String buildEmailContent(StudentDto student, List<CourseDto> courses) {
        StringBuilder content = new StringBuilder();
        content.append("Chào ").append(student.getFullName()).append(",\n\n");
        content.append("Dưới đây là danh sách các khóa học bạn có thể đăng ký:\n\n");

        for (CourseDto course : courses) {
            content.append("- ").append(course.getName()).append("\n");
            content.append("  Mô tả: ").append(course.getDescription()).append("\n");
            content.append("  Bắt đầu: ").append(course.getStartDate()).append("\n");
            content.append("  Kết thúc: ").append(course.getEndDate()).append("\n\n");
        }

        content.append("Hãy đăng nhập vào hệ thống để đăng ký các khóa học này.\n\n");
        content.append("Trân trọng,\n");
        content.append("CourseHub Team");

        return content.toString();
    }
}

package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Student;

import java.util.List;

public interface EmailService {
    void sendStudentReminderEmail(StudentDto student, List<CourseDto> courses);

}

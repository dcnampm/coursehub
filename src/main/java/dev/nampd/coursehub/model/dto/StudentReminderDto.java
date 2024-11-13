package dev.nampd.coursehub.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class StudentReminderDto {
    //for sending email
    private StudentDto student;
    private List<CourseDto> courses;

    public StudentReminderDto(StudentDto student, List<CourseDto> courses) {
        this.student = student;
        this.courses = courses;
    }
}

package dev.nampd.coursehub.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StudentCourseDto {
    //for sending email
    private StudentDto student;
    private CourseDto course;

    public StudentCourseDto(StudentDto student, CourseDto course) {}
}

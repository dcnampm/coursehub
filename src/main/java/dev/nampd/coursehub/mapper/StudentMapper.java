package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.EnrollmentDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Role;
import dev.nampd.coursehub.model.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentMapper {
    private final EnrollmentMapper enrollmentMapper;

    public StudentMapper(EnrollmentMapper enrollmentMapper) {
        this.enrollmentMapper = enrollmentMapper;
    }

    public StudentDto toStudentDto(Student student) {
        List<EnrollmentDto> enrollments = student.getEnrollments().stream()
                .map(enrollmentMapper::toEnrollmentDto)
                .toList();

        return new StudentDto(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getPassword(),
                student.getRole().toString(),
                enrollments
        );
    }

    public Student toStudent(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }
        Student student = new Student();

        student.setFullName(studentDto.getFullName());
        student.setEmail(studentDto.getEmail());
        student.setPassword(studentDto.getPassword());
        student.setRole(Role.valueOf(studentDto.getRole()));

        return student;
    }
}

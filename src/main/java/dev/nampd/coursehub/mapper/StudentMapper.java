package dev.nampd.coursehub.mapper;

import dev.nampd.coursehub.model.dto.EnrollmentDTO;
import dev.nampd.coursehub.model.dto.StudentDTO;
import dev.nampd.coursehub.model.entity.Role;
import dev.nampd.coursehub.model.entity.Student;

import java.util.List;

public class StudentMapper {
    private final EnrollmentMapper enrollmentMapper;

    public StudentMapper(EnrollmentMapper enrollmentMapper) {
        this.enrollmentMapper = enrollmentMapper;
    }

    public StudentDTO toStudentDTO(Student student) {
        List<EnrollmentDTO> enrollments = student.getEnrollments().stream()
                .map(enrollmentMapper::toEnrollmentDTO)
                .toList();

        return new StudentDTO(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getPassword(),
                student.getRole().toString(),
                enrollments
        );
    }

    public Student toStudent(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }
        Student student = new Student();

        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(studentDTO.getPassword());
        student.setRole(Role.valueOf(studentDTO.getRole()));

        return student;
    }
}

package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.response.PagedResponse;

import java.util.List;

public interface StudentService {
    void createStudent(StudentDto studentDto);
    List<StudentDto> getAllStudents();
    StudentDto getStudentById(Long id);

    StudentDto getStudentByEmail(String email);

    void updateStudent(Long id, StudentDto studentDto);
    void deleteStudent(Long id);

    PagedResponse<StudentDto> getStudentsPaginated(int page, int size);
}

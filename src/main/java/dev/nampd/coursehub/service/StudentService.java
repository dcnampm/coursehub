package dev.nampd.coursehub.service;

import dev.nampd.coursehub.model.dto.RegisterDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.response.PagedResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface StudentService {
    @PreAuthorize("hasRole('ADMIN')")
    void createStudent(StudentDto studentDto);
    @PreAuthorize("hasRole('ADMIN')")
    List<StudentDto> getAllStudents();
    @PreAuthorize("hasRole('ADMIN')")
    StudentDto getStudentById(Long id);
    @PreAuthorize("hasRole('ADMIN')")
    StudentDto getStudentByEmail(String email);
    @PreAuthorize("hasRole('ADMIN')")
    void updateStudent(Long id, StudentDto studentDto);
    @PreAuthorize("hasRole('ADMIN')")
    void deleteStudent(Long id);
    @PreAuthorize("hasRole('ADMIN')")
    PagedResponse<StudentDto> getStudentsPaginated(int page, int size);

    void registerNewStudent(RegisterDto registerDto);
}

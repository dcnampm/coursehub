package dev.nampd.coursehub.service.impl;

import dev.nampd.coursehub.mapper.StudentMapper;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Role;
import dev.nampd.coursehub.model.entity.Student;
import dev.nampd.coursehub.model.response.PagedResponse;
import dev.nampd.coursehub.repository.StudentRepository;
import dev.nampd.coursehub.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public void createStudent(StudentDto studentDto) {
        Student student = studentMapper.toStudent(studentDto);

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        studentRepository.save(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<StudentDto> getStudentsPaginated(int page, int size) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

        List<StudentDto> studentDtoList = studentPage
                .map(studentMapper::toStudentDto)
                .getContent();

        return new PagedResponse<>(
                studentDtoList,
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages()
        );
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentDto(student);
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentDto(student);
    }

    @Override
    public void updateStudent(Long id, StudentDto updatedStudentDto) {
        Student existingStudent = studentRepository.findById(updatedStudentDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        existingStudent.setFullName(updatedStudentDto.getFullName());
        existingStudent.setEmail(updatedStudentDto.getEmail());
        existingStudent.setPassword(updatedStudentDto.getPassword());
        existingStudent.setRole(Role.valueOf((updatedStudentDto.getRole())));

        studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }

        studentRepository.deleteById(id);
    }
}

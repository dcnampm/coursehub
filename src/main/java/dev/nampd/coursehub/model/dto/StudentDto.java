package dev.nampd.coursehub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private List<EnrollmentDto> enrollments;
}

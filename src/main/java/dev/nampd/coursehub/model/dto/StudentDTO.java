package dev.nampd.coursehub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private List<EnrollmentDTO> enrollments;
}

package dev.nampd.coursehub.model.dto;

import dev.nampd.coursehub.security.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class RegisterDto {
    @Size(min = 2, message = "Tên phải có tối thiểu 2 ký tự")
    private String fullName;
    @Email(message = "Email không đúng", regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @Size(min = 4, message = "Password phải có tối thiểu 4 ký tự")
    private String password;
    private String confirmPassword;
}

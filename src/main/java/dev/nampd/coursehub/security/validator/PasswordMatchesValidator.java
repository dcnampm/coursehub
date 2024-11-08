package dev.nampd.coursehub.security.validator;

import dev.nampd.coursehub.model.dto.RegisterDto;
import dev.nampd.coursehub.security.annotation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterDto> {

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext context) {
        return registerDto.getPassword() != null && registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }
}

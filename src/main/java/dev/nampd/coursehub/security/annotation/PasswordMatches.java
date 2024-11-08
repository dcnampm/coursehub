package dev.nampd.coursehub.security.annotation;

import dev.nampd.coursehub.security.validator.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Password và Confirm Password phải trùng khớp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

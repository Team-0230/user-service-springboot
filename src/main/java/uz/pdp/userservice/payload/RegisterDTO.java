package uz.pdp.userservice.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDTO(
        @NotBlank(message = "First name must not be empty")
        String firstName,
        @NotBlank(message = "Last name must not be empty")
        String lastName,
        @Email(message = "Email is not valid",
                regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                flags = Pattern.Flag.CASE_INSENSITIVE)
        @NotBlank(message = "Email must not be empty")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password,
        @NotBlank(message = "Password confirm must not be empty")
        String confirmPassword
) {
}

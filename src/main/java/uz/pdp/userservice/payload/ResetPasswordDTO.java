package uz.pdp.userservice.payload;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO(
        @NotBlank(message = "Email must not be empty")
        String password,
        @NotBlank(message = "Email must not be empty")
        String confirmPassword
) {
}

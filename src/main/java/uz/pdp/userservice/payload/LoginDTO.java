package uz.pdp.userservice.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Email must not be empty")
        @Email(message = "Email is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password
) {

}

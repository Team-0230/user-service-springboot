package uz.pdp.userservice.payload;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationDTO(
        @NotBlank(message = "Email verification code is not valid")
        String code
) {

}

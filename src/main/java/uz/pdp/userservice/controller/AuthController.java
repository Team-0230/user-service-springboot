package uz.pdp.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.userservice.payload.ApiResponse;
import uz.pdp.userservice.payload.EmailVerificationDTO;
import uz.pdp.userservice.payload.LoginDTO;
import uz.pdp.userservice.payload.RegisterDTO;
import uz.pdp.userservice.service.AuthService;
import uz.pdp.userservice.utils.AppConstants;
import uz.pdp.userservice.payload.ResetPasswordDTO;

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    public static final String BASE_URL = "/auth";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String VERIFY_EMAIL_URL = "/verify-email/{email}";
    public static final String GET_VERIFICATION_URL = "/get-verification/{email}";
    public static final String GET_PERMISSIONS_URL = "/get-permissions";
    public static final String RESET_PASSWORD_URL = "/reset-password";
    private final AuthService authService;

    @PostMapping(REGISTER_URL)
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @PostMapping(VERIFY_EMAIL_URL)
    public ResponseEntity<ApiResponse> verifyEmail(
            @PathVariable String email,
            @RequestBody @Valid EmailVerificationDTO emailVerificationDTO
    ) {
        return ResponseEntity.ok(authService.verifyEmail(email, emailVerificationDTO));
    }

    @GetMapping(GET_VERIFICATION_URL)
    public ResponseEntity<ApiResponse> getVerificationCode(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(authService.getVerificationCode(email));
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<ApiResponse> login(
            @RequestBody @Valid LoginDTO loginDTO
    ) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @GetMapping(RESET_PASSWORD_URL)
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(authService.getResetPassword(email));
    }

    @PostMapping(RESET_PASSWORD_URL+"/{code}")
    public ResponseEntity<ApiResponse> resetPassword(
            @PathVariable String code,
            @RequestBody @Valid ResetPasswordDTO resetPasswordDTO
    ) {
        return ResponseEntity.ok(authService.resetPassword(code, resetPasswordDTO));
    }

    @GetMapping(GET_PERMISSIONS_URL)
    public ResponseEntity<ApiResponse> getPermissions() {
        return ResponseEntity.ok(authService.getPermissions());
    }
}

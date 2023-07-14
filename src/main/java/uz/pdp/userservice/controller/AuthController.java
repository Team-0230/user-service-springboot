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

@RestController(AuthController.BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    public static final String BASE_URL = "/auth";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String VERIFY_EMAIL_URL = "/verify-email/{email}";
    public static final String GET_VERIFICATION_URL = "/get-verification/{email}";
    private final AuthService authService;

    @PostMapping(REGISTER_URL)
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        return authService.register(registerDTO);
    }

    @PostMapping(VERIFY_EMAIL_URL)
    public ResponseEntity<ApiResponse> verifyEmail(
            @PathVariable String email,
            @RequestBody @Valid EmailVerificationDTO emailVerificationDTO
    ) {
        return authService.verifyEmail(email, emailVerificationDTO);
    }

    @GetMapping(GET_VERIFICATION_URL)
    public ResponseEntity<ApiResponse> getVerificationCode(
            @PathVariable String email
    ) {
        return authService.getVerificationCode(email);
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<ApiResponse> login(
            @RequestBody @Valid LoginDTO loginDTO
    ) {
        return authService.login(loginDTO);
    }
}

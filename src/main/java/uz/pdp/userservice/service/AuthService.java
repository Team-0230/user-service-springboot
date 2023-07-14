package uz.pdp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.exception.BadRequestDetailsException;
import uz.pdp.userservice.exception.ResourceNotFoundException;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.model.VerificationCode;
import uz.pdp.userservice.payload.*;
import uz.pdp.userservice.repository.UserRepository;
import uz.pdp.userservice.service.mapper.RegisterDTOMapper;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RegisterDTOMapper registerDTOMapper;
    private final UserRepository userRepository;
    private final VerificationCodeService verificationCodeService;
    private final NotificationService notificationService;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public ResponseEntity<ApiResponse> register(RegisterDTO registerDTO) {
        if (Objects.equals(registerDTO.password(), registerDTO.confirmPassword())) {
            throw new BadRequestDetailsException("Passwords do not match");
        }
        if (userRepository.existsByEmail(registerDTO.email())) {
            throw new BadRequestDetailsException("Email already exists");
        }
        User user = registerDTOMapper.apply(registerDTO);
        user = userRepository.save(user);
        VerificationCode verificationCode = verificationCodeService.generateVerificationCode(user);

        SendEMailDTO eMailDTO = verificationCodeService.generateVerificationEmail(user, verificationCode);
        notificationService.sendSimpleEmail(eMailDTO);

        return ResponseEntity
                .ok(new ApiResponse(HttpStatus.OK, true, "Verification code sent to your email"));
    }

    public ResponseEntity<ApiResponse> verifyEmail(String email, EmailVerificationDTO emailVerificationDTO) {
        User user = getUserByEmail(email);
        if (user.isVerifiedEmail() || user.getVerificationCode() == null) {
            throw new BadRequestDetailsException("Email already verified");
        }
        VerificationCode verificationCode = user.getVerificationCode();
        verificationCodeService.checkVerificationCode(verificationCode, emailVerificationDTO);
        user.setVerifiedEmail(true);
        return ResponseEntity
                .ok(new ApiResponse(HttpStatus.OK, true, "Email verified"));
    }

    public ResponseEntity<ApiResponse> getVerificationCode(String email) {
        User user = getUserByEmail(email);
        if (user.isVerifiedEmail()) {
            throw new BadRequestDetailsException("Email already verified");
        }
        VerificationCode verificationCode = verificationCodeService.generateVerificationCode(user);

        SendEMailDTO mailDTO = verificationCodeService.generateVerificationEmail(user, verificationCode);
        notificationService.sendSimpleEmail(mailDTO);

        return ResponseEntity
                .ok(new ApiResponse(HttpStatus.OK, true, "Verification code sent to your email"));
    }

    public ResponseEntity<ApiResponse> login(LoginDTO loginDTO) {

        return null;
    }
}

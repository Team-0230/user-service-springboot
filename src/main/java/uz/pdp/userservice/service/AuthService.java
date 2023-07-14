package uz.pdp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.exception.BadRequestDetailsException;
import uz.pdp.userservice.exception.ResourceNotFoundException;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.model.VerificationCode;
import uz.pdp.userservice.model.enums.PermissionEnum;
import uz.pdp.userservice.payload.*;
import uz.pdp.userservice.repository.UserRepository;
import uz.pdp.userservice.service.mapper.RegisterDTOMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

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

    public ApiResponse register(RegisterDTO registerDTO) {
        if (!Objects.equals(registerDTO.password(), registerDTO.confirmPassword())) {
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

        return new ApiResponse(HttpStatus.OK, true, "Verification code sent to your email");
    }

    public ApiResponse verifyEmail(String email, EmailVerificationDTO emailVerificationDTO) {
        User user = getUserByEmail(email);
        if (user.isVerifiedEmail() || user.getVerificationCode() == null) {
            throw new BadRequestDetailsException("Email already verified");
        }
        VerificationCode verificationCode = user.getVerificationCode();
        verificationCodeService.checkVerificationCode(verificationCode, emailVerificationDTO);
        user.setVerifiedEmail(true);
        return new ApiResponse(HttpStatus.OK, true, "Email verified");
    }

    public ApiResponse getVerificationCode(String email) {
        User user = getUserByEmail(email);
        if (user.isVerifiedEmail()) {
            throw new BadRequestDetailsException("Email already verified");
        }
        VerificationCode verificationCode = user.getVerificationCode();
        if (!Objects.isNull(verificationCode)) {

            if (verificationCode.getResendDate().isBefore(LocalDateTime.now())) {
                verificationCodeService.deleteVerificationCode(verificationCode);
            } else {
                throw new BadRequestDetailsException(
                        "To resend verification code, please wait %s seconds"
                                .formatted(LocalDateTime
                                        .now().until(verificationCode.getResendDate(), ChronoUnit.SECONDS)
                ));
            }
        }
        verificationCode = verificationCodeService.generateVerificationCode(user);

        SendEMailDTO mailDTO = verificationCodeService.generateVerificationEmail(user, verificationCode);
        notificationService.sendSimpleEmail(mailDTO);

        return new ApiResponse(HttpStatus.OK, true, "Verification code sent to your email");
    }

    public ApiResponse login(LoginDTO loginDTO) {
        return null;
    }

    public ApiResponse getResetPassword(String email) {
        User user = getUserByEmail(email);
        if (!user.isVerifiedEmail()) {
            throw new BadRequestDetailsException("Email not verified");
        }
        UUID resetPassword = UUID.randomUUID();
        user.setResetPasswordCode(resetPassword);

        String msg = "To reset your password, please click the link below:\n" +
                "http://localhost:8080/user/auth/reset-password/%s".formatted(resetPassword);
        notificationService.sendSimpleEmail(new SendEMailDTO(email, "Reset password", msg));

        return new ApiResponse(HttpStatus.OK, true, "Reset password link sent to your email");
    }

    public ApiResponse resetPassword(String code, ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.getUserByResetPasswordCode(UUID.fromString(code))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!Objects.equals(resetPasswordDTO.password(), resetPasswordDTO.confirmPassword())) {
            throw new BadRequestDetailsException("Passwords do not match");
        }
        user.setPassword(resetPasswordDTO.password());
        user.setResetPasswordCode(null);
        userRepository.save(user);
        return new ApiResponse(HttpStatus.OK, true, "Password reset successfully");
    }

    public ApiResponse getPermissions() {
        return new ApiResponse(HttpStatus.OK, true, "success", PermissionEnum.values());
    }
}

package uz.pdp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.exception.BadRequestDetailsException;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.model.VerificationCode;
import uz.pdp.userservice.payload.EmailVerificationDTO;
import uz.pdp.userservice.payload.SendEMailDTO;
import uz.pdp.userservice.repository.VerificationCodeRepo;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepo verificationCodeRepo;

    @Value("${auth.verification-code.length}")
    private int verificationCodeLength;
    @Value("${auth.verification-code.validity-duration}")
    private int verificationCodeValidityDuration;
    @Value("${auth.verification-code.resend-duration}")
    private int verificationCodeResendDuration;
    @Value("${auth.verification-code.request-limit}")
    private int verificationCodeRequestLimit;

    public VerificationCode generateVerificationCode(User user) {
        if (user.getVerificationCode() != null) {
            verificationCodeRepo.deleteByUser(user);
        }
        VerificationCode verificationCode = new VerificationCode(
                user,
                generateNumericPassword(verificationCodeLength),
                LocalDateTime.now().plusSeconds(verificationCodeValidityDuration)
        );
        verificationCode = verificationCodeRepo.save(verificationCode);
        return verificationCode;
    }

    public static String generateNumericPassword(int length) {
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);  // Generate random digit (0-9)
            passwordBuilder.append(digit);
        }
        return passwordBuilder.toString();
    }

    public void checkVerificationCode(VerificationCode verificationCode, EmailVerificationDTO verificationDTO) {
        if (verificationCode.isUsed() || verificationCode.isExpired()) {
            throw new BadRequestDetailsException("Verification code expired");
        }
        if (verificationCode.getExpiredDate().isBefore(LocalDateTime.now())) {
            verificationCode.setExpired(true);
            verificationCodeRepo.save(verificationCode);
            throw new BadRequestDetailsException("Verification code expired");
        }
        if (verificationCode.isBlocked()) {
            throw new BadRequestDetailsException("Verification code blocked");
        }
        if (verificationCode.getCountRequest() >= verificationCodeRequestLimit) {
            verificationCode.setBlocked(true);
            verificationCodeRepo.save(verificationCode);
            throw new BadRequestDetailsException("Verification code blocked");
        }
        verificationCode.setCountRequest(verificationCode.getCountRequest() + 1);
        verificationCodeRepo.save(verificationCode);
        if (!verificationCode.getCode().equals(verificationDTO.code())) {
            throw new BadRequestDetailsException("Verification code is wrong");
        }
        verificationCode.setUsed(true);
        verificationCodeRepo.save(verificationCode);
    }

    public SendEMailDTO generateVerificationEmail(User user, VerificationCode verificationCode) {
        String text = """
                Hello %s,
                Your verification code is %s
                Your verification code will expire in %s minutes
                Your verification link is %s
                """;
        return new SendEMailDTO(user.getEmail(),
                "Verification code",
                text.formatted(
                        user.getFirstName(),
                        verificationCode.getCode(),
                        verificationCodeValidityDuration / 60,
                        "http://localhost:8080/user/auth/verify-email/%s".formatted(
                                user.getEmail()
                        )
                ));
    }
}

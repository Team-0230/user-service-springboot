package uz.pdp.userservice.payload;

public record SendEMailDTO(
        String email,
        String subject,
        String text
) {
}

package uz.pdp.userservice.payload;

import org.springframework.http.HttpStatus;

public record ApiResponse(
        HttpStatus status,
        boolean success,
        String message
) {
}

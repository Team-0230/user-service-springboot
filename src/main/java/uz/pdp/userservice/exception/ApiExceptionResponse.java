package uz.pdp.userservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ApiExceptionResponse(
        String message,
        HttpStatus status,
        LocalDateTime timestamp
) {
}

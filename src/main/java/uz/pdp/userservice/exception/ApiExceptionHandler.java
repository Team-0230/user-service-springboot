package uz.pdp.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {
            ResourceNotFoundException.class
    })
    public ResponseEntity<ApiExceptionResponse> handleNotFoundException(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                new ApiExceptionResponse(
                        e.getMessage(),
                        status,
                        LocalDateTime.now()
                ),
                status
        );
    }

    @ExceptionHandler(value = {
            BadRequestDetailsException.class
    })
    public ResponseEntity<ApiExceptionResponse> handleBadRequestException(BadRequestDetailsException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ApiExceptionResponse(
                        e.getMessage(),
                        status,
                        LocalDateTime.now()
                ),
                status
        );
    }

    @ExceptionHandler(value = {
            DataNotLoadedException.class,
            JsonProcessingException.class
    })
    public ResponseEntity<ApiExceptionResponse> handleInternalServerErrorException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                new ApiExceptionResponse(
                        e.getMessage(),
                        status,
                        LocalDateTime.now()
                ),
                status
        );
    }
}

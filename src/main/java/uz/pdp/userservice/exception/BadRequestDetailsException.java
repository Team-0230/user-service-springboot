package uz.pdp.userservice.exception;

public class BadRequestDetailsException extends RuntimeException {
    public BadRequestDetailsException(String message) {
        super(message);
    }
}

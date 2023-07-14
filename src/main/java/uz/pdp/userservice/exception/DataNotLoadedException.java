package uz.pdp.userservice.exception;

public class DataNotLoadedException extends RuntimeException {
    public DataNotLoadedException(String message) {
        super(message);
    }
}

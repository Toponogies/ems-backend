package vn.com.tma.emsbackend.exception;

public class ResourceConstraintViolationException extends RuntimeException {
    public ResourceConstraintViolationException(String message) {
        super(message);
    }
}

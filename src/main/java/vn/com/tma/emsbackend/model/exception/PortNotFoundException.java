package vn.com.tma.emsbackend.model.exception;

public class PortNotFoundException extends RuntimeException {
    public PortNotFoundException(Long id) {
        super("Port with id '" + id + "' does not exist.");
    }
}

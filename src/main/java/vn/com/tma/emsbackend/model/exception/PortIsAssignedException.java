package vn.com.tma.emsbackend.model.exception;

public class PortIsAssignedException extends RuntimeException {
    public PortIsAssignedException(Long id) {
        super("Port with id '" + id + "' is assigned.");
    }
}

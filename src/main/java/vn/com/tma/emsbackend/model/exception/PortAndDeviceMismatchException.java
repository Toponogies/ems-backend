package vn.com.tma.emsbackend.model.exception;

public class PortAndDeviceMismatchException extends RuntimeException {
    public PortAndDeviceMismatchException() {
        super("Port and device are mismatched.");
    }
}

package vn.com.tma.emsbackend.model.exception;

public class DeviceLabelExistsException extends RuntimeException {
    public DeviceLabelExistsException(String label) {
        super("Device with label '" + label + "' already exists.");
    }
}

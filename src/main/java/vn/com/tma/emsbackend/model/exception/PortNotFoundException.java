package vn.com.tma.emsbackend.model.exception;

public class PortNotFoundException extends RuntimeException {
    public PortNotFoundException(Long id) {
        super("Port with id '" + id + "' does not exist.");
    }

    public PortNotFoundException(String name, String deviceLabel) {
        super("Port with name '" + name + "' does not belong to device with label '" + deviceLabel + " '");
    }
}

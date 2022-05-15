package vn.com.tma.emsbackend.model.exception;

public class DeviceIPExistsException extends RuntimeException {
    public DeviceIPExistsException(String ip) {
        super("Device with ip '" + ip + "' already exists.");
    }
}

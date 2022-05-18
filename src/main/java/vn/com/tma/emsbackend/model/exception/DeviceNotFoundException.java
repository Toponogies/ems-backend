package vn.com.tma.emsbackend.model.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String target) {
        super("Device with id or ip '" + target + "' does not exist.");
    }

}

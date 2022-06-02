package vn.com.tma.emsbackend.model.exception;

public class DeviceCannotBeUpdatedException extends RuntimeException {
    public DeviceCannotBeUpdatedException() {
        super("Device cannot be updated");
    }
}

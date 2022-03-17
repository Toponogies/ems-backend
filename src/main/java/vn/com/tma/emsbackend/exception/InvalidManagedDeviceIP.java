package vn.com.tma.emsbackend.exception;

public class InvalidManagedDeviceIP extends RuntimeException {
    public InvalidManagedDeviceIP() {
        super("Managed element IP address is invalid. Please enter a valid IP address");
    }
}

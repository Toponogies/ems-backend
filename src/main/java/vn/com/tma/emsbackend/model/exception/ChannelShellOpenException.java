package vn.com.tma.emsbackend.model.exception;

public class ChannelShellOpenException extends DeviceConnectionException {
    public ChannelShellOpenException(Long deviceId) {
        super("Can not open channel shell on device with id: " + deviceId);
    }
}

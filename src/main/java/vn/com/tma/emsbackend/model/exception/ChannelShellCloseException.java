package vn.com.tma.emsbackend.model.exception;

public class ChannelShellCloseException extends DeviceConnectionException {

    public ChannelShellCloseException(Long deviceId) {
        super("Can not close channel shell on device with id: " + deviceId);
    }
}

package vn.com.tma.emsbackend.model.exception;

public class DeviceConnectionException extends  RuntimeException{
    public DeviceConnectionException(Long deviceId) {
        super("Can not establish connection with device with id:" + deviceId);
    }
}

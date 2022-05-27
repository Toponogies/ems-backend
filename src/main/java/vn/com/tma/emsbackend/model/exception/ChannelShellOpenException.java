package vn.com.tma.emsbackend.model.exception;

public class ChannelShellOpenException extends RuntimeException {
    public ChannelShellOpenException(Throwable cause) {
        super("Can not open channel shell", cause);
    }
}

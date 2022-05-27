package vn.com.tma.emsbackend.model.exception;

public class ChannelShellCloseException extends RuntimeException {

    public ChannelShellCloseException(Throwable cause) {
        super("Can not close channel shell", cause);
    }
}

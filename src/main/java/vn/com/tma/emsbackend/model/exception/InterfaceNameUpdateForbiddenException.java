package vn.com.tma.emsbackend.model.exception;

public class InterfaceNameUpdateForbiddenException extends RuntimeException {
    public InterfaceNameUpdateForbiddenException() {
        super("Cannot update interface name.");
    }
}

package vn.com.tma.emsbackend.model.exception;

public class InterfaceNotFoundException extends RuntimeException {
    public InterfaceNotFoundException(Long id) {
        super("Interface with id or having port id '" + id + "' does not exist.");
    }
}

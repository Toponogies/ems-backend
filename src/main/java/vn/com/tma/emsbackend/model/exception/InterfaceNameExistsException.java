package vn.com.tma.emsbackend.model.exception;

public class InterfaceNameExistsException extends RuntimeException {
    public InterfaceNameExistsException(String name) {
        super("Interface with name '" + name + "' already exists.");
    }
}

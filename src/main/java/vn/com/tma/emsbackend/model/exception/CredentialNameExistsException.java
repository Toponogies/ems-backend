package vn.com.tma.emsbackend.model.exception;

public class CredentialNameExistsException extends RuntimeException {
    public CredentialNameExistsException(String name) {
        super("Credential with name '" + name + "' already exists.");
    }
}

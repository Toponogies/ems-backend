package vn.com.tma.emsbackend.model.exception;

public class CredentialNotFoundException extends RuntimeException {

    public CredentialNotFoundException(Long id) {
        super("Credential with id '" + id + "' does not exist.");
    }
}

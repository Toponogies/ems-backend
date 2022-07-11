package vn.com.tma.emsbackend.model.exception;

public class CredentialNotFoundException extends RuntimeException {

    public CredentialNotFoundException(String target) {
        super("Credential with id or name '" + target + "' does not exist.");
    }
}

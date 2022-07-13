package vn.com.tma.emsbackend.model.exception;

public class CredentialLinkedToDeviceException extends RuntimeException {

    public CredentialLinkedToDeviceException(Long id) {
        super("Credential with id '" + id + "' is linked to at least 1 device.");
    }
}

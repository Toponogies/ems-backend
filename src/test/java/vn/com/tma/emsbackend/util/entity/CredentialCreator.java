package vn.com.tma.emsbackend.util.entity;

import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.entity.Credential;

public class CredentialCreator {
    public static Credential createCredentialBy(CredentialDto credentialDto) {
        Credential credential = new Credential();
        credential.setId(credentialDto.getId());
        credential.setUsername(credentialDto.getUsername());
        credential.setName(credentialDto.getName());
        credential.setPassword(credentialDto.getPassword());
        return credential;
    }

    public static CredentialDto createCredentialDtoBy(Credential credential) {
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setId(credential.getId());
        credentialDto.setUsername(credential.getUsername());
        credentialDto.setName(credential.getName());
        credentialDto.setPassword(credential.getPassword());
        return credentialDto;
    }
}

package vn.com.tma.emsbackend.util.entity;

import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;

public class CredentialCreator {
    public static Credential createCredentialBy(CredentialDTO credentialDto) {
        Credential credential = new Credential();
        credential.setId(credentialDto.getId());
        credential.setUsername(credentialDto.getUsername());
        credential.setName(credentialDto.getName());
        credential.setPassword(credentialDto.getPassword());
        return credential;
    }

    public static CredentialDTO createCredentialDtoBy(Credential credential) {
        CredentialDTO credentialDto = new CredentialDTO();
        credentialDto.setId(credential.getId());
        credentialDto.setUsername(credential.getUsername());
        credentialDto.setName(credential.getName());
        credentialDto.setPassword(credential.getPassword());
        return credentialDto;
    }
}

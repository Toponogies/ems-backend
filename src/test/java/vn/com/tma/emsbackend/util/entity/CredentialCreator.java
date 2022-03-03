package vn.com.tma.emsbackend.util.entity;

import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.entity.Credential;

public class CredentialCreator {
    public static Credential createCredentialBy(CredentialRequestDto credentialRequestDto) {
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setUsername(credentialRequestDto.getUsername());
        credential.setName(credentialRequestDto.getName());
        credential.setPassword(credentialRequestDto.getPassword());
        return credential;
    }

    public static CredentialDto createCredentialDtoBy(Credential credential) {
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setId(credential.getId());
        credentialDto.setUsername(credential.getUsername());
        credentialDto.setName(credential.getName());
        return credentialDto;
    }

    public static CredentialDto createCredentialDtoBy(CredentialRequestDto credentialRequestDto) {
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setId(1L);
        credentialDto.setUsername(credentialRequestDto.getUsername());
        credentialDto.setName(credentialRequestDto.getName());
        return credentialDto;
    }

}

package vn.com.tma.emsbackend.service.credential;

import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;

import java.util.List;

public interface CredentialService {
     List<CredentialDto> getAll();

     CredentialDto get(long id);

     CredentialDto add(CredentialRequestDto credentialRequestDto);

     CredentialDto update(long id, CredentialRequestDto credentialRequestDto);

     void delete(long id);
}

package vn.com.tma.emsbackend.service.credential;

import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.service.Service;

public interface CredentialService extends Service<CredentialDTO> {
    boolean existsById(Long id);
}
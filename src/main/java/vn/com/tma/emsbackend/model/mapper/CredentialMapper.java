package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper extends IMapper<Credential, CredentialDTO> {
    Credential dtoToEntity(CredentialDTO credentialDto);

    CredentialDTO entityToDTO(Credential credential);
}

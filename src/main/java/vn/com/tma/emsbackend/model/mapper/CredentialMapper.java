package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper extends IMapper<Credential, CredentialDTO> {
    @Mapping(target = "devices", ignore = true)
    Credential dtoToEntity(CredentialDTO credentialDto);

    CredentialDTO entityToDTO(Credential credential);
}

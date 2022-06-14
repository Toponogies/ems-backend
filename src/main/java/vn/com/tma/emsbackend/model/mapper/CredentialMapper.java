package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CredentialMapper extends IMapper<Credential, CredentialDTO> {
    @Mapping(target = "devices", ignore = true)
    Credential dtoToEntity(CredentialDTO credentialDto);

    @Mapping(target = "devices", source = "credential.devices", qualifiedByName = "getDevices")
    CredentialDTO entityToDTO(Credential credential);

    @Named("getDevices")
    default String getDevices(List<NetworkDevice> devices) {
        return devices.stream().map(NetworkDevice::getLabel).collect(Collectors.joining(", "));
    }
}

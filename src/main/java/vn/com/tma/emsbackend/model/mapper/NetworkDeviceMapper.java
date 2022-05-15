package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;

@Mapper(componentModel = "spring")
public interface NetworkDeviceMapper extends IMapper<NetworkDevice, NetworkDeviceDTO> {
    @Mapping(target = "credential", ignore = true)
    @Mapping(target = "firmware", ignore = true)
    @Mapping(target = "serial", ignore = true)
    @Mapping(target = "macAddress", ignore = true)
    @Mapping(target = "deviceType", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "ports", ignore = true)
    NetworkDevice dtoToEntity(NetworkDeviceDTO networkDeviceDTO);

    @Mapping(target = "credentialId", source = "networkDevice.credential.id")
    NetworkDeviceDTO entityToDTO(NetworkDevice networkDevice);
}

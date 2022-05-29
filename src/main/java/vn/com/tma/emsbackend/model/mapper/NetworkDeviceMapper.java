package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.tma.emsbackend.common.enums.Enum;
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
    @Mapping(target = "ports", ignore = true)
    @Mapping(target = "interfaces", ignore = true)
    @Mapping(target = "state", source = "state", qualifiedByName = "stringToState")
    NetworkDevice dtoToEntity(NetworkDeviceDTO networkDeviceDTO);

    @Mapping(target = "credentialId", source = "networkDevice.credential.id")
    NetworkDeviceDTO entityToDTO(NetworkDevice networkDevice);

    @Named("stringToState")
    static Enum.NetworkDeviceState stringToState(String state) {
        return java.lang.Enum.valueOf(Enum.NetworkDeviceState.class, state);
    }
}

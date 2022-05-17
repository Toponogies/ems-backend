package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;

@Mapper(componentModel = "spring")
public interface PortMapper extends IMapper<Port, PortDTO> {
    @Mapping(target = "anInterface", ignore = true)
    @Mapping(target = "networkDevice", ignore = true)
    Port dtoToEntity(PortDTO portDTO);

    @Mapping(target = "networkDeviceId", source = "port.networkDevice.id")
    @Mapping(target = "anInterface", ignore = true)
    PortDTO entityToDTO(Port port);
}

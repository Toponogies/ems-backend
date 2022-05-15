package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;

@Mapper(componentModel = "spring")
public interface PortMapper extends IMapper<Port, PortDTO> {
    Port dtoToEntity(PortDTO portDTO);

    @Mapping(target = "networkDeviceId", source = "port.networkDevice.id")
    PortDTO entityToDTO(Port port);
}

package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Interface;

@Mapper(componentModel = "spring")
public interface InterfaceMapper extends IMapper<Interface, InterfaceDTO> {
    @Mapping(target = "port", ignore = true)
    @Mapping(target = "networkDevice", ignore = true)
    Interface dtoToEntity(InterfaceDTO interfaceDTO);

    @Mapping(target = "networkDeviceId", source = "anInterface.networkDevice.id")
    @Mapping(target = "portId", source = "anInterface.port.id")
    InterfaceDTO entityToDTO(Interface anInterface);
}
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

    @Mapping(target = "networkDevice", source = "anInterface.networkDevice.label")
    @Mapping(target = "port", source = "anInterface.port.name")
    InterfaceDTO entityToDTO(Interface anInterface);
}
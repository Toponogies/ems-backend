package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Interface;

@Mapper(componentModel = "spring")
public interface InterfaceMapper extends IMapper<Interface, InterfaceDTO> {
    @Mapping(target = "port", ignore = true)
    @Mapping(target = "networkDevice", ignore = true)
    @Mapping(target = "state", source = "state", qualifiedByName = "stringToState")
    @Mapping(target = "dhcp", source = "dhcp", qualifiedByName = "stringToState")
    Interface dtoToEntity(InterfaceDTO interfaceDTO);

    @Mapping(target = "networkDeviceId", source = "anInterface.networkDevice.id")
    @Mapping(target = "portId", source = "anInterface.port.id")
    InterfaceDTO entityToDTO(Interface anInterface);

    @Named("stringToState")
    static Enum.State stringToState(String state) {
        return java.lang.Enum.valueOf(Enum.State.class, state);
    }
}
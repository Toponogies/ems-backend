package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PortMapper extends IMapper<Port, PortDTO> {
    @Mapping(target = "interfaces", ignore = true)
    @Mapping(target = "networkDevice", ignore = true)
    @Mapping(target = "state", ignore = true)
    Port dtoToEntity(PortDTO portDTO);

    @Mapping(target = "networkDevice", source = "port.networkDevice.label")
    @Mapping(target = "interfaces", source = "port.interfaces", qualifiedByName = "getInterfaces")
    PortDTO entityToDTO(Port port);

    @Named("getInterfaces")
    default String getInterfaces(List<Interface> interfaces) {
        return interfaces.stream().map(Interface::getName).collect(Collectors.joining(", "));
    }
}

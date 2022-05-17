package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;

@Mapper(componentModel = "spring")
public interface PortMapper extends IMapper<Port, PortDTO> {
    @Mapping(target = "anInterface", ignore = true)
    @Mapping(target = "networkDevice", ignore = true)
    @Mapping(target = "state", source = "state", qualifiedByName = "stringToState")
    Port dtoToEntity(PortDTO portDTO);

    @Mapping(target = "networkDeviceId", source = "port.networkDevice.id")
    PortDTO entityToDTO(Port port);

    @Named("stringToState")
    static Enum.State stringToState(String state) {
        return java.lang.Enum.valueOf(Enum.State.class, state);
    }
}

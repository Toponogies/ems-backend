package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.tma.emsbackend.model.dto.NTPServerDTO;
import vn.com.tma.emsbackend.model.entity.NTPServer;

@Mapper(componentModel = "spring")
public interface NTPServerMapper extends IMapper<NTPServer, NTPServerDTO> {
    @Mapping(target = "networkDevice", ignore = true)
    NTPServer dtoToEntity(NTPServerDTO ntpServerDTO);

    @Mapping(target = "networkDeviceId",  source = "ntpServer.networkDevice.id")
    NTPServerDTO entityToDto(NTPServer ntpServer);

}

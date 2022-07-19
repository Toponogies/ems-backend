package vn.com.tma.emsbackend.util.entity.creator;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;

import java.util.ArrayList;
import java.util.List;

public class PortCreator {
    public static Port createEntityBy(PortDTO portDTO) {
        Port port = new Port();
        port.setId(portDTO.getId());
        port.setConnector(portDTO.getConnector());
        port.setMacAddress(portDTO.getMacAddress());
        port.setName(portDTO.getName());
        port.setState(Enum.State.valueOf(portDTO.getState()));
        return port;
    }

    public static PortDTO createDtoBy(Port port) {
        PortDTO portDTO = new PortDTO();
        portDTO.setId(port.getId());
        portDTO.setConnector(port.getConnector());
        portDTO.setNetworkDevice(port.getNetworkDevice().getLabel());
        portDTO.setMacAddress(port.getMacAddress());
        portDTO.setName(port.getName());
        portDTO.setState(port.getState().toString());
        return portDTO;
    }

    public static List<PortDTO> createDtosBy(List<Port> ports){
        List<PortDTO> portDTOS = new ArrayList<>();
        for(Port port:ports){
            portDTOS.add(createDtoBy(port));
        }
        return  portDTOS;
    }
}

package vn.com.tma.emsbackend.util.entity.creator;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Interface;

public class InterfaceCreator {
    public static Interface createCredentialBy(InterfaceDTO interfaceDTO) {
        Interface anInterface = new Interface();
        anInterface.setId(interfaceDTO.getId());
        anInterface.setDhcp(Enum.State.valueOf(interfaceDTO.toString()));
        anInterface.setGateway(interfaceDTO.getGateway());
        anInterface.setIpAddress(interfaceDTO.getIpAddress());
        anInterface.setName(interfaceDTO.getName());
        anInterface.setNetmask(interfaceDTO.getNetmask());
        anInterface.setState(Enum.State.valueOf(interfaceDTO.toString()));
        return anInterface;
    }

    public static InterfaceDTO createDtoBy(Interface anInterface) {
        InterfaceDTO interfaceDTO = new InterfaceDTO();
        interfaceDTO.setId(anInterface.getId());
        interfaceDTO.setDhcp(anInterface.getDhcp().toString());
        interfaceDTO.setGateway(anInterface.getGateway());
        interfaceDTO.setIpAddress(anInterface.getIpAddress());
        interfaceDTO.setName(anInterface.getName());
        interfaceDTO.setNetmask(anInterface.getNetmask());
        interfaceDTO.setState(anInterface.getState().toString());
        interfaceDTO.setPort(anInterface.getPort().getName());
        interfaceDTO.setNetworkDevice(anInterface.getNetworkDevice().getLabel());
        return interfaceDTO;
    }
}

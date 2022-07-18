package vn.com.tma.emsbackend.util.entity.Creator;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;

public class NetworkDeviceCreator {
    public static NetworkDevice createEntityBy(NetworkDeviceDTO networkDeviceDTO) {
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(networkDevice.getId());
        networkDevice.setSshPort(networkDeviceDTO.getSshPort());
        networkDevice.setMacAddress(networkDeviceDTO.getMacAddress());
        networkDevice.setIpAddress(networkDeviceDTO.getIpAddress());
        networkDevice.setLabel(networkDeviceDTO.getLabel());
        networkDevice.setModel(networkDeviceDTO.getModel());
        networkDevice.setFirmware(networkDevice.getFirmware());
        networkDevice.setSerial(networkDevice.getSerial());
        networkDevice.setState(Enum.NetworkDeviceState.valueOf(networkDeviceDTO.getState()));
        networkDevice.setDeviceType(Enum.NetworkDeviceType.valueOf(networkDeviceDTO.getDeviceType()));
        Credential credential = new Credential();
        credential.setName(networkDeviceDTO.getCredential());
        networkDevice.setCredential(credential);
        return networkDevice;
    }

    public static NetworkDeviceDTO createDTOBy(NetworkDevice networkDevice){
        NetworkDeviceDTO networkDeviceDTO = new NetworkDeviceDTO();
        networkDeviceDTO.setId(networkDevice.getId());
        networkDeviceDTO.setFirmware(networkDevice.getFirmware());
        networkDeviceDTO.setIpAddress(networkDevice.getIpAddress());
        networkDeviceDTO.setLabel(networkDevice.getLabel());
        networkDeviceDTO.setMacAddress(networkDevice.getMacAddress());
        networkDeviceDTO.setModel(networkDevice.getModel());
        networkDeviceDTO.setSerial(networkDevice.getSerial());
        networkDeviceDTO.setCredential(networkDevice.getCredential().getName());
        networkDeviceDTO.setSshPort(networkDevice.getSshPort());
        networkDeviceDTO.setState(networkDevice.getState().toString());
        networkDeviceDTO.setDeviceType(networkDevice.getDeviceType().toString());
        return networkDeviceDTO;
    }
}

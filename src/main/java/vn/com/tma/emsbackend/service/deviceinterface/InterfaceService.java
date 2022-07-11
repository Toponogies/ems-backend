package vn.com.tma.emsbackend.service.deviceinterface;

import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.service.Service;

import java.util.List;

public interface InterfaceService extends Service<InterfaceDTO> {
    List<InterfaceDTO> getByNetworkDevice(Long deviceId);

    InterfaceDTO getByPort(Long portId);

    void resyncInterfaceByDeviceId(Long deviceId);

    List<InterfaceDTO> getByNetworkDeviceLabel(String deviceLabel);
}

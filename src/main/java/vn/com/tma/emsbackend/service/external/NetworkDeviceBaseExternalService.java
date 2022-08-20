package vn.com.tma.emsbackend.service.external;

import vn.com.tma.emsbackend.model.entity.NetworkDevice;

public interface NetworkDeviceBaseExternalService {
    public NetworkDevice getNetworkDeviceDetail(Long id);

    public String sendCommand(Long id, String command);
}

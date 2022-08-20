package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.external.NetworkDeviceBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.NetworkDeviceSSHService;

@Service
@RequiredArgsConstructor
public class NetworkDeviceCommonService {
    private final NetworkDeviceSSHService networkDeviceSSHService;

    public NetworkDevice getNetworkDeviceDetail(NetworkDevice networkDevice){
        NetworkDeviceBaseExternalService networkDeviceExternalService = getNetworkDeviceExternalService(networkDevice);
        return networkDeviceExternalService.getNetworkDeviceDetail(networkDevice.getId());
    }

    public String sendCommand(NetworkDevice networkDevice, String command){
        NetworkDeviceBaseExternalService networkDeviceExternalService = getNetworkDeviceExternalService(networkDevice);
        return networkDeviceExternalService.sendCommand(networkDevice.getId() ,command);
    }

    private NetworkDeviceBaseExternalService getNetworkDeviceExternalService(NetworkDevice networkDevice){
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return networkDeviceSSHService;
    }
}

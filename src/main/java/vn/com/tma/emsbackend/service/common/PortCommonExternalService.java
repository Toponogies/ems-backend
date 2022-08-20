package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.service.external.PortBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.PortSSHService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortCommonExternalService {
    private PortSSHService portSSHService;

    public List<Port> getAllPort(NetworkDevice networkDevice) {
        PortBaseExternalService portBaseExternalService = getPortExternalService(networkDevice);
        return portSSHService.getAllPort(networkDevice.getId());
    }

    private PortBaseExternalService getPortExternalService(NetworkDevice networkDevice){
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return portSSHService;
    }
}

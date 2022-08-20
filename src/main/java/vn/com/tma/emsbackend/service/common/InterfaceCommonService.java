package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.external.InterfaceBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.InterfaceSSHService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class InterfaceCommonService {
    private final InterfaceSSHService interfaceSSHService;

    public List<Interface> getAllInterface(NetworkDevice networkDevice, List<PortDTO> ports) {
        InterfaceBaseExternalService interfaceExternalService  = getInterfaceExternalService(networkDevice);
        return interfaceExternalService.getAllInterface(networkDevice.getId(), ports);
    }

    public void add(NetworkDevice networkDevice, Interface anInterface) {
        InterfaceBaseExternalService interfaceExternalService  = getInterfaceExternalService(networkDevice);
        interfaceExternalService.add(anInterface);
    }

    public void edit(NetworkDevice networkDevice, String oldInterfaceName, Interface anInterface) {
        InterfaceBaseExternalService interfaceExternalService  = getInterfaceExternalService(networkDevice);
        interfaceExternalService.edit(oldInterfaceName, anInterface);
    }

    public void delete(NetworkDevice networkDevice, Interface anInterface) {
        InterfaceBaseExternalService interfaceExternalService  = getInterfaceExternalService(networkDevice);
        interfaceExternalService.delete(anInterface);
    }

    private InterfaceBaseExternalService getInterfaceExternalService(NetworkDevice networkDevice){
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return interfaceSSHService;
    }
}

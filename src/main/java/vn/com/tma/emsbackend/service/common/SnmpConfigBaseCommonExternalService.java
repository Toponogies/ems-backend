package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.external.SnmpConfigBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.SnmpConfigSSHService;

@Service
@RequiredArgsConstructor
public class SnmpConfigBaseCommonExternalService {
    private final SnmpConfigSSHService snmpConfigSSHService;

    public void setSnmpTrapConfig(NetworkDevice networkDevice) {
        SnmpConfigBaseExternalService snmpConfigBaseExternalService = getSnmpConfigExternalService(networkDevice);
        snmpConfigBaseExternalService.setSnmpTrapConfig(networkDevice.getId());
    }

    private SnmpConfigBaseExternalService getSnmpConfigExternalService(NetworkDevice networkDevice) {
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return snmpConfigSSHService;
    }
}

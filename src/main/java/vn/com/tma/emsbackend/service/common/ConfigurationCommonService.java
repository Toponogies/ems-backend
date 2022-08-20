package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.external.ConfigurationBaseExternalService;
import vn.com.tma.emsbackend.service.external.NetworkDeviceBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.ConfigurationSSHService;

@Service
@RequiredArgsConstructor
public class ConfigurationCommonService {
    private final ConfigurationSSHService configurationSSHService;

    public String exportDeviceConfig(NetworkDevice networkDevice){
        ConfigurationBaseExternalService configurationExternalService = getConfigurationExternalService(networkDevice);
        return configurationExternalService.exportDeviceConfig(networkDevice.getId());
    }

    private ConfigurationBaseExternalService getConfigurationExternalService(NetworkDevice networkDevice){
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return configurationSSHService;
    }

}

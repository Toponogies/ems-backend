package vn.com.tma.emsbackend.service.ssh;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.com.tma.emsbackend.repository.ssh.ConfigurationSSHRepository;
import vn.com.tma.emsbackend.service.external.ConfigurationBaseExternalService;

@Service
@RequiredArgsConstructor
public class ConfigurationSSHService implements ConfigurationBaseExternalService {
    private final ConfigurationSSHRepository configurationSSHRepository;

    public String exportDeviceConfig(long deviceId){
        return configurationSSHRepository.exportDeviceConfig(deviceId);
    }

}

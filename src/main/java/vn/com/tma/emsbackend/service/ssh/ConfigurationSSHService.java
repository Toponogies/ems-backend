package vn.com.tma.emsbackend.service.ssh;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.com.tma.emsbackend.repository.ssh.ConfigurationSSHRepository;

@Service
@RequiredArgsConstructor
public class ConfigurationSSHService {
    private final ConfigurationSSHRepository configurationSSHRepository;

    public String exportDeviceConfig(long deviceId){
        return configurationSSHRepository.exportDeviceConfig(deviceId);
    }

}

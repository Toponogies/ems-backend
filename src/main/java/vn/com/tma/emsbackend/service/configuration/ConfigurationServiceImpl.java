package vn.com.tma.emsbackend.service.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.ssh.ConfigurationSSHService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final NetworkDeviceService networkDeviceService;

    private final ConfigurationSSHService networkDeviceSSHService;


    @Override
    @Transactional
    public byte[] downloadDeviceConfigFileById(Long id) throws DeviceNotFoundException {
        boolean checkIfExistedById = networkDeviceService.existsById(id);
        if (!checkIfExistedById) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
        String result = networkDeviceSSHService.exportDeviceConfig(id);
        return result.getBytes();
    }
}

package vn.com.tma.emsbackend.service.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.commandgenerator.ConfigurationCommandGenerator;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.ssh.NetworkDeviceSSHService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final NetworkDeviceService networkDeviceService;

    private final NetworkDeviceSSHService networkDeviceSSHService;


    @Override
    @Transactional
    public byte[] downloadDeviceConfigFileById(Long id) {
        boolean checkIfExistedById = networkDeviceService.existsById(id);
        if (!checkIfExistedById) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
        String result = networkDeviceSSHService.sendCommand(id, ConfigurationCommandGenerator.export());
        return result.getBytes();
    }
}

package vn.com.tma.emsbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.service.ssh.utils.DeviceConnectionManager;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

@Configuration
public class DeviceConnectionManagerConfig {
    @Bean
    @Transactional
    public DeviceConnectionManager deviceConnectionManager(NetworkDeviceRepository networkDeviceRepository) {
        return new DeviceConnectionManager(networkDeviceRepository);
    }
}

package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceConnectionManager {

    private final Map<Long, SSHExecutor> sshExecutorHashMap = new ConcurrentHashMap<>();

    private final NetworkDeviceRepository networkDeviceRepository;


    public void addNewConnection(NetworkDevice networkDevice) {
        SSHExecutor sshExecutor = new SSHExecutor();
        sshExecutor.open(networkDevice);
        sshExecutorHashMap.put(networkDevice.getId(), sshExecutor);

    }

    public void removeConnection(Long id) {
        sshExecutorHashMap.get(id).close();
        sshExecutorHashMap.remove(id);
    }

    public SSHExecutor getConnection(Long networkDeviceId) {
        SSHExecutor sshExecutor = sshExecutorHashMap.get(networkDeviceId);
        Optional<NetworkDevice> optionalNetworkDevice = networkDeviceRepository.findById(networkDeviceId);
        if (optionalNetworkDevice.isEmpty()) {
            sshExecutorHashMap.remove(networkDeviceId);
            throw new DeviceNotFoundException(networkDeviceId.toString());
        }

        NetworkDevice networkDevice = optionalNetworkDevice.get();
        if (sshExecutor == null || sshExecutor.isClosed()) {
            if(sshExecutor != null) removeConnection(networkDeviceId);
            addNewConnection(networkDevice);
            sshExecutor = sshExecutorHashMap.get(networkDeviceId);
        } else {
            if (!optionalNetworkDevice.get().equals(sshExecutor.getCurrentManagedDevice())) {
                removeConnection(networkDeviceId);
                addNewConnection(networkDevice);
                sshExecutor = sshExecutorHashMap.get(networkDeviceId);
            }
        }

        if (sshExecutor == null) {
            throw new DeviceConnectionException(networkDeviceId);
        }
        return sshExecutor;
    }
}

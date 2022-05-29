package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.extern.slf4j.Slf4j;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class DeviceConnectionManager {

    private final Map<Long, SSHExecutor> sshExecutorHashMap;

    private final NetworkDeviceRepository networkDeviceRepository;


    public DeviceConnectionManager(NetworkDeviceRepository networkDeviceRepository) {
        sshExecutorHashMap = new ConcurrentHashMap<>();

        this.networkDeviceRepository = networkDeviceRepository;
        List<NetworkDevice> networkDevices = this.networkDeviceRepository.findAll();

        for (NetworkDevice networkDevice : networkDevices) {
            addNewConnection(networkDevice);
        }
    }

    public void addNewConnection(NetworkDevice networkDevice) {
        SSHExecutor sshExecutor = new SSHExecutor();
        try {
            sshExecutor.open(networkDevice);
            sshExecutorHashMap.put(networkDevice.getId(), sshExecutor);
        } catch (DeviceConnectionException e) {
            log.error(e.getMessage(), e);
        }
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

        if (sshExecutor == null || !sshExecutor.isOpen()) {
            addNewConnection(optionalNetworkDevice.get());
            sshExecutor = sshExecutorHashMap.get(networkDeviceId);
        } else {
            if (!optionalNetworkDevice.get().equals(sshExecutor.getCurrentManagedDevice())) {
                removeConnection(networkDeviceId);
                addNewConnection(optionalNetworkDevice.get());
                sshExecutor = sshExecutorHashMap.get(networkDeviceId);
            }
        }

        if (sshExecutor == null) throw new DeviceConnectionException(networkDeviceId);
        return sshExecutor;
    }
}

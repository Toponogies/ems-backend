package vn.com.tma.emsbackend.common;

import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeviceConnectionManager {

    private final Map<Long, SSHExecutor> sshExecutorHashMap;


    public DeviceConnectionManager(NetworkDeviceRepository networkDeviceRepository) {
        sshExecutorHashMap = new HashMap<>();

        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();

        for (NetworkDevice networkDevice : networkDevices) {
            addNewConnection(networkDevice);
        }
    }

    public void addNewConnection(NetworkDevice networkDevice) {
        SSHExecutor sshExecutor = new SSHExecutor();
        sshExecutor.open(networkDevice);
        sshExecutorHashMap.put(networkDevice.getId(), sshExecutor);
    }

    public void removeConnection(Long id) {
        sshExecutorHashMap.remove(id);
    }

    public SSHExecutor getConnection(Long networkDeviceId){
        return sshExecutorHashMap.get(networkDeviceId);
    }
}

package vn.com.tma.emsbackend.common;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DeviceConnectionManager {

    private final Map<Long, SSHExecutor> sshExecutorHashMap;

    @Autowired
    NetworkDeviceRepository networkDeviceRepository;


    public DeviceConnectionManager( NetworkDeviceRepository networkDeviceRepository) {
        sshExecutorHashMap = new HashMap<>();

        this.networkDeviceRepository = networkDeviceRepository;
        List<NetworkDevice> networkDevices = this.networkDeviceRepository.findAll();

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
        SSHExecutor sshExecutor = sshExecutorHashMap.get(networkDeviceId);
        if(sshExecutor == null){
            Optional<NetworkDevice> optionalNetworkDevice = networkDeviceRepository.findById(networkDeviceId);
            if(optionalNetworkDevice.isEmpty()){
                throw new DeviceNotFoundException(networkDeviceId.toString());
            }
            addNewConnection(optionalNetworkDevice.get());
            sshExecutor = sshExecutorHashMap.get(networkDeviceId);
        }
        return sshExecutor;
    }
}

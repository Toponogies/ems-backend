package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.ssh.NetworkDeviceSSHRepository;

@RequiredArgsConstructor
@Service
public class NetworkDeviceSSHService {
    private final NetworkDeviceSSHRepository networkDeviceSSHRepository;

    public NetworkDevice getNetworkDeviceDetail(Long id){
        return networkDeviceSSHRepository.getDetail(id);
    }

    public String sendCommand(Long id, String command){
        return networkDeviceSSHRepository.sendCommand(id, command);
    }
}
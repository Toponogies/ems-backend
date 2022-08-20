package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.ssh.NetworkDeviceSSHRepository;
import vn.com.tma.emsbackend.service.external.NetworkDeviceBaseExternalService;

@RequiredArgsConstructor
@Service
public class NetworkDeviceSSHService implements NetworkDeviceBaseExternalService {
    private final NetworkDeviceSSHRepository networkDeviceSSHRepository;

    @Override
    public NetworkDevice getNetworkDeviceDetail(Long id) {
        return networkDeviceSSHRepository.getDetail(id);
    }

    @Override
    public String sendCommand(Long id, String command) {
        return networkDeviceSSHRepository.sendCommand(id, command);
    }
}
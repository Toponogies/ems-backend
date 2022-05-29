package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.repository.ssh.PortSSHRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortSSHService {
    private final PortSSHRepository portSSHRepository;

    public List<Port> getAllPort(long deviceId){
        return portSSHRepository.getAll(deviceId);
    }
}
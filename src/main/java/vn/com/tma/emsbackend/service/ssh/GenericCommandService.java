package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;

@Service
@RequiredArgsConstructor
public class GenericCommandService {
    private final NetworkDeviceSSHService networkDeviceSSHService;

    @Transactional
    public String sendCommandToDeviceById(Long id, SSHCommandDTO sshCommandDTO) {
        return networkDeviceSSHService.sendCommand(id, sshCommandDTO.getCommand());
    }
}

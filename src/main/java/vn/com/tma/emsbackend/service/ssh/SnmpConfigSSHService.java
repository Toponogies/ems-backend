package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.repository.ssh.SnmpConfigSSHRepository;
import vn.com.tma.emsbackend.service.external.SnmpConfigBaseExternalService;

@Service
@RequiredArgsConstructor
public class SnmpConfigSSHService implements SnmpConfigBaseExternalService {
    private final SnmpConfigSSHRepository snmpConfigSSHRepository;
    @Override
    public void setSnmpTrapConfig(Long deviceId) {
        snmpConfigSSHRepository.setSnmpTrapConfig(deviceId);
    }
}

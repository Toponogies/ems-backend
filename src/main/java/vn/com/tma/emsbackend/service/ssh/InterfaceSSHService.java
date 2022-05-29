package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.repository.ssh.InterfaceSSHRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterfaceSSHService {
    private final InterfaceSSHRepository interfaceSSHRepository;

    public List<Interface> getAllInterface(long networkDeviceId, List<Port> ports) {
        return interfaceSSHRepository.getAll(networkDeviceId, ports);
    }

    public void add(Interface anInterface) {
        interfaceSSHRepository.add(anInterface);
    }
    public void edit(String oldInterfaceName, Interface anInterface){
        interfaceSSHRepository.edit(oldInterfaceName, anInterface);
    }

    public void delete(Interface anInterface){
        interfaceSSHRepository.delete(anInterface);
    }
}
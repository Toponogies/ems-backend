package vn.com.tma.emsbackend.service.deviceinterface;

import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.service.Service;

import java.util.List;

public interface InterfaceService extends Service<InterfaceDTO> {
    List<InterfaceDTO> getByNetworkDevice(Long deviceId);

    InterfaceDTO getByPort(Long portId);

    @Transactional
    void resyncInterface(long deviceId);
}

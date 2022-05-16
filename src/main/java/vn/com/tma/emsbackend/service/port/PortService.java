package vn.com.tma.emsbackend.service.port;

import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.service.Service;

import java.util.List;

public interface PortService extends Service<PortDTO> {
    List<PortDTO> getByNetworkDevice(Long deviceId);
}

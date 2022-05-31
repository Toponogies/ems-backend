package vn.com.tma.emsbackend.service.port;

import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.service.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PortService extends Service<PortDTO> {
    List<PortDTO> getByNetworkDevice(Long deviceId);

    boolean existsById(Long id);

    Optional<Port> getById(Long id);

    void resyncPortByDeviceId(Long deviceId);

    Port getById(Long id, Long deviceId);

    List<Port> getByDeviceId(Long deviceId);
}

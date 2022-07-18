package vn.com.tma.emsbackend.service.device;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.service.Service;

import java.util.List;

public interface NetworkDeviceService extends Service<NetworkDeviceDTO> {
    NetworkDeviceDTO getByIpAddress(String ipAddress);

    List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType);

    boolean existsById(Long id);

    NetworkDeviceDTO getByLabel(String label);

    void resyncDeviceDetailById(Long id);

    void updateStateById(Long id, Enum.NetworkDeviceState state);

    boolean existByLabel(String deviceLabel);
}


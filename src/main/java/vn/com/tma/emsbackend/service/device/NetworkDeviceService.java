package vn.com.tma.emsbackend.service.device;

import java.util.List;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.service.Service;

public interface NetworkDeviceService extends Service<NetworkDeviceDTO> {
    NetworkDeviceDTO getByIpAddress(String ipAddress);

    List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType);
}

package vn.com.tma.emsbackend.service.networkdevice;

import java.util.List;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.dto.NetworkDeviceDto;
import vn.com.tma.emsbackend.service.Service;

public interface NetworkDeviceService extends Service<NetworkDeviceDto> {
    NetworkDeviceDto getByIpAddress(String ipAddress);

    List<NetworkDeviceDto> getByDeviceType(Enum.NetworkDeviceType deviceType);
}

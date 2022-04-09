package vn.com.tma.emsbackend.service.networkdevice;

import java.util.List;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.dto.NetworkDeviceDto;
import vn.com.tma.emsbackend.dto.NetworkDeviceRequestDto;

public interface NetworkDeviceService {
    List<NetworkDeviceDto> getAll();

    NetworkDeviceDto getById(long id);

    NetworkDeviceDto getByIpAddress(String ipAddress);

    List<NetworkDeviceDto> getByDeviceType(Enum.NetworkDeviceType deviceType);

    NetworkDeviceDto add(NetworkDeviceRequestDto networkDeviceRequestDto);

    void delete(long id);

    NetworkDeviceDto update(long id, NetworkDeviceRequestDto networkDeviceRequestDto);
}

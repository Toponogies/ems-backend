package vn.com.tma.emsbackend.service.manageddevice;

import java.util.List;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;

public interface ManagedDeviceService {
    List<ManagedDeviceDto> getAll();

    ManagedDeviceDto getById(long id);

    ManagedDeviceDto getByIpAddress(String ipAddress);

    List<ManagedDeviceDto> getByDeviceType(Enum.ManagedDeviceType deviceType);

    ManagedDeviceDto add(ManagedDeviceRequestDto managedDeviceRequestDto);

    void delete(long id);

    ManagedDeviceDto update(long id, ManagedDeviceRequestDto managedDeviceRequestDto);
}

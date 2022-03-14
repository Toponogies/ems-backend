package vn.com.tma.emsbackend.service.manageddevice;

import java.util.List;

import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;

public interface ManagedDeviceService {
    public List<ManagedDeviceDto> getAll();

    public ManagedDeviceDto get(long id);

    ManagedDeviceDto add(ManagedDeviceRequestDto managedDeviceRequestDto);

    public void delete(long id);

    public ManagedDeviceDto update(long id, ManagedDeviceRequestDto managedDeviceRequestDto);
}

package vn.com.tma.emsbackend.service.device;

import java.util.List;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandResponseDTO;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.Service;

public interface NetworkDeviceService extends Service<NetworkDeviceDTO> {
    NetworkDeviceDTO getByIpAddress(String ipAddress);

    List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType);

    boolean existsById(Long id);

    void resyncDeviceDetailById(Long id);

    void addDevicesToResyncQueueById(List<Long> id);

    void updateStateById(Long id, Enum.NetworkDeviceState state);

    SSHCommandResponseDTO sendCommandToDeviceById(Long id, SSHCommandDTO sshCommandDTO);

    byte[] downloadDeviceConfigFileById(Long deviceId);
}


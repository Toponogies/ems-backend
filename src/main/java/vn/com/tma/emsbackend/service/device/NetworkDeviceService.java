package vn.com.tma.emsbackend.service.device;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandResponseDTO;
import vn.com.tma.emsbackend.service.Service;

import java.util.List;

public interface NetworkDeviceService extends Service<NetworkDeviceDTO> {
    NetworkDeviceDTO getByIpAddress(String ipAddress);

    List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType);

    boolean existsById(Long id);

    NetworkDeviceDTO getByLabel(String label);

    void resyncDeviceDetailById(Long id);

    void addDevicesToResyncQueueById(List<Long> id);

    void updateStateById(Long id, Enum.NetworkDeviceState state);

    SSHCommandResponseDTO sendCommandToDeviceById(Long id, SSHCommandDTO sshCommandDTO);

    byte[] downloadDeviceConfigFileById(Long deviceId);
}


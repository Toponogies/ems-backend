package vn.com.tma.emsbackend.service.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.ResyncQueueManagement;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.*;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.service.credential.CredentialService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.service.ntpserver.NTPServerService;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.service.ssh.NetworkDeviceSSHService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NetworkDeviceServiceImpl implements NetworkDeviceService {
    private final NetworkDeviceRepository networkDeviceRepository;

    private final NetworkDeviceSSHService networkDeviceSSHService;

    private final NetworkDeviceMapper networkDeviceMapper;

    private final CredentialService credentialService;

    private final ResyncQueueManagement resyncQueueManagement;


    @Override
    public List<NetworkDeviceDTO> getAll() {
        log.info("Get all network device");

        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
        for (NetworkDevice networkDevice : networkDevices) {
            networkDevice.setResyncing(resyncQueueManagement.isDeviceResyncing(networkDevice.getId()));
        }
        return networkDeviceMapper.entitiesToDTOs(networkDevices);
    }

    @Override
    public NetworkDeviceDTO get(long id) {
        log.info("Get network device with id: {}", id);

        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
        NetworkDevice networkDevice = networkDeviceOptional.get();
        networkDevice.setResyncing(resyncQueueManagement.isDeviceResyncing(networkDevice.getId()));

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    public NetworkDeviceDTO getByIpAddress(String ipAddress) {
        log.info("Get network device with ip address: {}", ipAddress);

        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress(ipAddress);

        if (networkDevice == null) {
            throw new DeviceNotFoundException(ipAddress);
        }
        networkDevice.setResyncing(resyncQueueManagement.isDeviceResyncing(networkDevice.getId()));

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    public List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType) {
        log.info("Get all network device with device type: {}", deviceType.name());

        return networkDeviceMapper.entitiesToDTOs(networkDeviceRepository.findAllByDeviceType(deviceType));
    }

    @Override
    @Transactional
    public NetworkDeviceDTO add(NetworkDeviceDTO networkDeviceDTO) {
        log.info("Add new device");

        boolean checkIfCredentialExisted = credentialService.existsById(networkDeviceDTO.getCredentialId());
        if (!checkIfCredentialExisted) {
            throw new CredentialNotFoundException(networkDeviceDTO.getCredentialId());
        }

        boolean checkIfExistedByLabel = networkDeviceRepository.existsByLabel(networkDeviceDTO.getLabel());
        if (checkIfExistedByLabel) {
            throw new DeviceLabelExistsException(networkDeviceDTO.getLabel());
        }

        boolean checkIfExistedByIpAddress = networkDeviceRepository.existsByIpAddress(networkDeviceDTO.getIpAddress());
        if (checkIfExistedByIpAddress) {
            throw new DeviceIPExistsException(networkDeviceDTO.getIpAddress());
        }

        networkDeviceDTO.setState(Enum.NetworkDeviceState.OUT_OF_SERVICE.toString());
        NetworkDevice networkDevice = networkDeviceMapper.dtoToEntity(networkDeviceDTO);

        Credential credential = new Credential();
        credential.setId(networkDeviceDTO.getCredentialId());
        networkDevice.setCredential(credential);

        networkDevice = networkDeviceRepository.save(networkDevice);
        resyncQueueManagement.pushToQueue(networkDevice.getId());

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    @Transactional
    public NetworkDeviceDTO update(long id, NetworkDeviceDTO networkDeviceDTO) {
        log.info("Update network device with id: {}", id);

        NetworkDevice deviceDupLabel = networkDeviceRepository.findByLabel(networkDeviceDTO.getLabel());
        if (deviceDupLabel != null && !deviceDupLabel.getId().equals(id)) {
            throw new DeviceLabelExistsException(networkDeviceDTO.getLabel());
        }

        NetworkDevice deviceDupIP = networkDeviceRepository.findByIpAddress(networkDeviceDTO.getIpAddress());
        if (deviceDupIP != null && !deviceDupIP.getId().equals(id)) {
            throw new DeviceIPExistsException(networkDeviceDTO.getIpAddress());
        }

        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }

        boolean checkIfCredentialExisted = credentialService.existsById(networkDeviceDTO.getCredentialId());
        if (!checkIfCredentialExisted) {
            throw new CredentialNotFoundException(networkDeviceDTO.getCredentialId());
        }

        NetworkDevice networkDevice = networkDeviceMapper.dtoToEntity(networkDeviceDTO);
        networkDevice.setId(id);

        // Set new credential
        Credential credential = new Credential();
        credential.setId(networkDeviceDTO.getCredentialId());
        networkDevice.setCredential(credential);

        networkDevice = networkDeviceRepository.save(networkDevice);
        resyncQueueManagement.pushToQueue(networkDevice.getId());

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    @Transactional
    public void delete(long id) {
        boolean checkIfExistedById = networkDeviceRepository.existsById(id);
        if (!checkIfExistedById) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }

        networkDeviceRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return networkDeviceRepository.existsById(id);
    }


    @Override
    public void resyncDeviceDetail(Long id) {
        NetworkDevice oldNetworkDevice = networkDeviceRepository.getById(id);
        NetworkDevice networkDevice = networkDeviceSSHService.getNetworkDeviceDetail(id);
        networkDevice.setIpAddress(oldNetworkDevice.getIpAddress());
        networkDevice.setState(Enum.NetworkDeviceState.IN_SERVICE);
        networkDevice.setCredential(oldNetworkDevice.getCredential());
        networkDevice.setLabel(oldNetworkDevice.getLabel());
        networkDevice.setSshPort(oldNetworkDevice.getSshPort());
        networkDevice.setId(id);
        networkDeviceRepository.save(networkDevice);
    }

    @Override
    public void resync(List<Long> ids) {
        resyncQueueManagement.pushToQueue(ids.toArray(new Long[0]));
    }

    @Override
    public void updateState(Long id, Enum.NetworkDeviceState state) {
        Optional<NetworkDevice> optionalNetworkDevice = networkDeviceRepository.findById(id);
        if (optionalNetworkDevice.isPresent()) {
            NetworkDevice networkDevice = optionalNetworkDevice.get();
            networkDevice.setState(state);
            networkDeviceRepository.save(networkDevice);
        } else {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
    }


}

package vn.com.tma.emsbackend.service.device;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.exception.CredentialNotFoundException;
import vn.com.tma.emsbackend.model.exception.DeviceLabelExistsException;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.service.credential.CredentialService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NetworkDeviceServiceImpl implements NetworkDeviceService {
    private final NetworkDeviceRepository networkDeviceRepository;
    private final NetworkDeviceMapper networkDeviceMapper;

    private final CredentialService credentialService;

    @Override
    public List<NetworkDeviceDTO> getAll() {
        log.info("Get all network device");

        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
        return networkDeviceMapper.entitiesToDTOs(networkDevices);
    }

    @Override
    public NetworkDeviceDTO get(long id) {
        log.info("Get network device with id: {}", id);

        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
        return networkDeviceMapper.entityToDTO(networkDeviceOptional.get());
    }

    @Override
    public NetworkDeviceDTO getByIpAddress(String ipAddress) {
        log.info("Get network device with ip address: {}", ipAddress);

        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress(ipAddress);

        if (networkDevice == null) {
            throw new DeviceNotFoundException(ipAddress);
        }

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    public List<NetworkDeviceDTO> getByDeviceType(Enum.NetworkDeviceType deviceType) {
        log.info("Get all network device with device type: {}", deviceType.name());

        return networkDeviceMapper.entitiesToDTOs(networkDeviceRepository.findAllByDeviceType(deviceType));
    }

    @Override
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

        NetworkDevice networkDevice = networkDeviceMapper.dtoToEntity(networkDeviceDTO);
        networkDevice.setState(Enum.NetworkDeviceState.OUT_OF_SERVICE);

        return networkDeviceMapper.entityToDTO(networkDeviceRepository.save(networkDevice));
    }

    @Override
    public NetworkDeviceDTO update(long id, NetworkDeviceDTO networkDeviceDTO) {
        log.info("Update network device with id: {}", id);

        boolean checkIfCredentialExisted = credentialService.existsById(networkDeviceDTO.getCredentialId());
        if (!checkIfCredentialExisted) {
            throw new CredentialNotFoundException(networkDeviceDTO.getCredentialId());
        }

        // Get device by id
        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }
        NetworkDevice networkDevice = networkDeviceOptional.get();

        // Update data
        networkDevice.setIpAddress(networkDeviceDTO.getIpAddress());
        networkDevice.setLabel(networkDeviceDTO.getLabel());
        networkDevice.setSshPort(networkDeviceDTO.getSshPort());

        // Set new credential
        Credential credential = new Credential();
        credential.setId(networkDeviceDTO.getCredentialId());
        networkDevice.setCredential(credential);

        networkDevice = networkDeviceRepository.save(networkDevice);

        return networkDeviceMapper.entityToDTO(networkDevice);
    }

    @Override
    public void delete(long id) {
        boolean checkIfExistedById = networkDeviceRepository.existsById(id);
        if (!checkIfExistedById) {
            throw new DeviceNotFoundException(String.valueOf(id));
        }

        networkDeviceRepository.deleteById(id);
    }
}

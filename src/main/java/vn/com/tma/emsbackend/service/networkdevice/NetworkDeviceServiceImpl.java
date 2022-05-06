package vn.com.tma.emsbackend.service.networkdevice;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.dto.NetworkDeviceDto;
import vn.com.tma.emsbackend.dto.NetworkDeviceRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.entity.NetworkDevice;
import vn.com.tma.emsbackend.entity.Port;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.repository.ssh.InterfaceSSHRepository;
import vn.com.tma.emsbackend.repository.ssh.NetworkDeviceSSHRepository;
import vn.com.tma.emsbackend.repository.ssh.PortSSHRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class NetworkDeviceServiceImpl implements NetworkDeviceService {
    private final NetworkDeviceRepository networkDeviceRepository;

    private final InterfaceSSHRepository interfaceSSHRepository;

    private final PortSSHRepository portSSHRepository;
    private final Mapper mapper;

    @Override
    public List<NetworkDeviceDto> getAll() {
        log.info("Get all network device");

        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
        return mapper.mapList(networkDevices, NetworkDeviceDto.class);
    }

    @Override
    public NetworkDeviceDto getById(long id) {
        log.info("Get network device with id:{}", id);
        Port port = new Port();
        port.setName("PORT-1");

        NDInterface ndInterface = new NDInterface();
        ndInterface.setName("test_interface_name");
        ndInterface.setIpAddress("1.1.1.1");
        ndInterface.setDhcp(Enum.InterfaceDHCP.ENABLED);
        ndInterface.setNetmask("255.255.255.0");

        interfaceSSHRepository.add(id,ndInterface,port);
        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            log.info(Constant.DEVICE_NOT_FOUND_BY_ID + id);
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND_BY_ID + id);
        }
        return mapper.map(networkDeviceOptional.get(), NetworkDeviceDto.class);
    }

    @Override
    public NetworkDeviceDto getByIpAddress(String ipAddress) {
        log.info("Get network device with ip address: {}", ipAddress);

        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress(ipAddress);

        //If not found device
        if (networkDevice == null) {
            log.info(Constant.DEVICE_NOT_FOUND_BY_IP_ADDRESS + ipAddress);
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND_BY_IP_ADDRESS + ipAddress);
        }

        return mapper.map(networkDevice, NetworkDeviceDto.class);
    }

    @Override
    public List<NetworkDeviceDto> getByDeviceType(Enum.NetworkDeviceType deviceType) {
        log.info("Get all network device with device type: {}", deviceType.name());

        return mapper.mapList(networkDeviceRepository.findByDeviceType(deviceType), NetworkDeviceDto.class);
    }


    @Override
    public void delete(long id) {
        try {
            networkDeviceRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + NetworkDevice.class.getName());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND_BY_ID + id);
        }
    }

    @Override
    public NetworkDeviceDto add(NetworkDeviceRequestDto networkDeviceRequestDto) {
        log.info("Add new device");

        NetworkDevice networkDevice = mapper.map(networkDeviceRequestDto, NetworkDevice.class);
        networkDevice.setState(Enum.NetworkDeviceState.OUT_OF_SERVICE);
        try {
            return mapper.map(networkDeviceRepository.save(networkDevice), NetworkDeviceDto.class);
        } catch (DataIntegrityViolationException e) {
            log.warn(Constant.CONSTRAINT_VIOLATED + NetworkDevice.class.getName());
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + NetworkDevice.class.getName());
        }
    }

    @Override
    public NetworkDeviceDto update(long id, NetworkDeviceRequestDto networkDeviceRequestDto) {
        log.info("Update network device with id:{}", id);
        // Get device by id
        Optional<NetworkDevice> networkDeviceOptional = networkDeviceRepository.findById(id);
        if (networkDeviceOptional.isEmpty()) {
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND_BY_ID + id);
        }
        NetworkDevice networkDevice = networkDeviceOptional.get();

        // Update data
        networkDevice.setIpAddress(networkDeviceRequestDto.getIpAddress());
        networkDevice.setLabel(networkDeviceRequestDto.getLabel());
        networkDevice.setSshPort(networkDeviceRequestDto.getPort());

        // set new credential
        Credential credential = new Credential();
        credential.setId(networkDeviceRequestDto.getCredentialId());
        networkDevice.setCredential(credential);

        try {
            networkDeviceRepository.save(networkDevice);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + NetworkDevice.class.getName());
        }
        return mapper.map(networkDevice, NetworkDeviceDto.class);
    }
}

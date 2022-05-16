package vn.com.tma.emsbackend.service.deviceinterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.*;
import vn.com.tma.emsbackend.model.mapper.InterfaceMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.port.PortService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceRepository interfaceRepository;

    private final InterfaceMapper interfaceMapper;

    private final NetworkDeviceService networkDeviceService;

    private final PortService portService;

    @Override
    public List<InterfaceDTO> getAll() {
        log.info("Get all interfaces");

        List<Interface> interfaces = interfaceRepository.findAll();
        return interfaceMapper.entitiesToDTOs(interfaces);
    }

    @Override
    public List<InterfaceDTO> getByNetworkDevice(Long deviceId) {
        log.info("Get all interface by device");

        boolean checkIfDeviceExisted = networkDeviceService.existsById(deviceId);
        if (!checkIfDeviceExisted) {
            throw new DeviceNotFoundException(String.valueOf(deviceId));
        }

        List<Interface> interfaces = interfaceRepository.findByNetworkDevice_Id(deviceId);
        return interfaceMapper.entitiesToDTOs(interfaces);
    }

    @Override
    public InterfaceDTO get(long id) {
        log.info("Get interface with id: {}", id);

        Optional<Interface> interfaceOptional = interfaceRepository.findById(id);
        if (interfaceOptional.isEmpty()) {
            throw new InterfaceNotFoundException(id);
        }
        return interfaceMapper.entityToDTO(interfaceOptional.get());
    }

    @Override
    public InterfaceDTO getByPort(Long portId) {
        log.info("Get interface by port with id: {}", portId);

        boolean checkIfPortExisted = portService.existsById(portId);
        if (!checkIfPortExisted) {
            throw new PortNotFoundException(portId);
        }

        Interface anInterface = interfaceRepository.findByPort_Id(portId);
        if (anInterface == null) {
            throw new InterfaceNotFoundException(portId);
        }

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    public InterfaceDTO add(InterfaceDTO interfaceDTO) {
        // Check device exist
        boolean checkIfDeviceExisted = networkDeviceService.existsById(interfaceDTO.getNetworkDeviceId());
        if (!checkIfDeviceExisted) {
            throw new DeviceNotFoundException(String.valueOf(interfaceDTO.getNetworkDeviceId()));
        }

        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(interfaceDTO.getNetworkDeviceId());

        Port port = null;
        if (interfaceDTO.getPortId() != null) {
            // Check if port exist
            PortDTO portDTO = portService.get(interfaceDTO.getPortId());

            // Check port's device is the same with interface device
            if (!portDTO.getId().equals(interfaceDTO.getNetworkDeviceId())) {
                throw new PortAndDeviceMismatchException();
            }

            port = new Port();
            port.setId(interfaceDTO.getPortId());
        }

        Interface anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);
        anInterface = interfaceRepository.save(anInterface);

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    public InterfaceDTO update(long id, InterfaceDTO interfaceDTO) {
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(interfaceDTO.getNetworkDeviceId());

        // Do not update device, can update port
        Optional<Interface> interfaceOptional = interfaceRepository.findById(id);
        if (interfaceOptional.isEmpty()) {
            throw new InterfaceNotFoundException(id);
        }

        // TODO: Valid update name
        Interface anInterface = interfaceOptional.get();
        if (interfaceDTO.getName().equals(anInterface.getName())) {
            throw new InterfaceNameUpdateForbiddenException();
        }

        Port port = null;
        if (interfaceDTO.getPortId() != null) {
            // Check if port exist
            PortDTO portDTO = portService.get(interfaceDTO.getPortId());

            // Check port's device is the same with interface device
            if (!portDTO.getId().equals(interfaceDTO.getNetworkDeviceId())) {
                throw new PortAndDeviceMismatchException();
            }

            port = new Port();
            port.setId(interfaceDTO.getPortId());
        }

        anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setId(id);
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);
        anInterface = interfaceRepository.save(anInterface);

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    public void delete(long id) {
        boolean checkIfExistedById = interfaceRepository.existsById(id);
        if (!checkIfExistedById) {
            throw new InterfaceNotFoundException(id);
        }

        interfaceRepository.deleteById(id);
    }
}

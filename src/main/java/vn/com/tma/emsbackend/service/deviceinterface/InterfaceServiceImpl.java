package vn.com.tma.emsbackend.service.deviceinterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.comparator.InterfaceComparator;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.*;
import vn.com.tma.emsbackend.model.mapper.InterfaceMapper;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.service.common.InterfaceCommonService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.port.PortService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceRepository interfaceRepository;

    private final InterfaceMapper interfaceMapper;

    private final NetworkDeviceService networkDeviceService;

    private final PortService portService;

    private final InterfaceCommonService interfaceCommonService;

    private final NetworkDeviceMapper networkDeviceMapper;

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
    @Transactional
    public InterfaceDTO add(InterfaceDTO interfaceDTO) {
        // Check duplicate name
        boolean checkIfNameExisted = interfaceRepository.existsByName(interfaceDTO.getName());
        if (checkIfNameExisted) {
            throw new InterfaceNameExistsException(interfaceDTO.getName());
        }

        // Check device exist
        NetworkDeviceDTO networkDeviceDTO = networkDeviceService.getByLabel(interfaceDTO.getNetworkDevice());
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(networkDeviceDTO.getId());

        // Check port exist
        PortDTO portDTO = portService.getByNameAndNetworkDevice(interfaceDTO.getPort(), interfaceDTO.getNetworkDevice());
        Port port = new Port();
        port.setId(portDTO.getId());
        port.setName(portDTO.getName());

        Interface anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);

        interfaceDTO = interfaceMapper.entityToDTO(interfaceRepository.save(anInterface));
        interfaceCommonService.add(networkDeviceMapper.dtoToEntity(networkDeviceDTO),anInterface);

        return interfaceDTO;
    }

    @Override
    @Transactional
    public InterfaceDTO update(long id, InterfaceDTO interfaceDTO) {
        Optional<Interface> interfaceOptional = interfaceRepository.findById(id);
        if (interfaceOptional.isEmpty()) {
            throw new InterfaceNotFoundException(id);
        }

        // Check duplicate name
        Interface interfaceDupName = interfaceRepository.findByName(interfaceDTO.getName());
        if (interfaceDupName != null && !interfaceDupName.getId().equals(id)) {
            throw new InterfaceNameExistsException(interfaceDTO.getName());
        }

        // NOTE: Do not update device, can update port
        if (!interfaceOptional.get().getNetworkDevice().getLabel().equals(interfaceDTO.getNetworkDevice())) {
            throw new DeviceCannotBeUpdatedException();
        }

        // Check device exist
        NetworkDeviceDTO networkDeviceDTO = networkDeviceService.getByLabel(interfaceDTO.getNetworkDevice());
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(networkDeviceDTO.getId());

        // Check port exist
        PortDTO portDTO = portService.getByNameAndNetworkDevice(interfaceDTO.getPort(), interfaceDTO.getNetworkDevice());
        Port port = new Port();
        port.setId(portDTO.getId());
        port.setName(portDTO.getName());

        Interface anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setId(id);
        String oldInterfaceName = anInterface.getName();
        anInterface.setName(interfaceDTO.getName());
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);
        anInterface = interfaceRepository.save(anInterface);

        interfaceCommonService.edit(networkDeviceMapper.dtoToEntity(networkDeviceDTO),oldInterfaceName, anInterface);

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<Interface> interfaceOptional = interfaceRepository.findById(id);
        if (interfaceOptional.isEmpty()) {
            throw new InterfaceNotFoundException(id);
        }
        Interface anInterface = interfaceOptional.get();

        interfaceRepository.deleteById(id);
        NetworkDeviceDTO networkDeviceDTO = networkDeviceService.get(anInterface.getNetworkDevice().getId());

        interfaceCommonService.delete(networkDeviceMapper.dtoToEntity(networkDeviceDTO),anInterface);
    }

    @Transactional
    @Override
    public void resyncInterfaceByDeviceId(Long deviceId) {
        List<Interface> oldInterfaces = interfaceRepository.findByNetworkDeviceId(deviceId);
        List<PortDTO> ports = portService.getByNetworkDevice(deviceId);
        NetworkDeviceDTO networkDeviceDTO = networkDeviceService.get(deviceId);
        List<Interface> newInterfaces = interfaceCommonService.getAllInterface(networkDeviceMapper.dtoToEntity(networkDeviceDTO), ports);

        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(deviceId);
        for (Interface ndInterface : newInterfaces) {
            ndInterface.setNetworkDevice(networkDevice);
        }

        syncWithDB(newInterfaces, oldInterfaces, new InterfaceComparator());

    }

    @Override
    public List<InterfaceDTO> getByNetworkDeviceLabel(String deviceLabel) {
        log.info("Get all interfaces by device label: {}", deviceLabel);

        boolean checkIfDeviceExisted = networkDeviceService.existByLabel(deviceLabel);
        if (!checkIfDeviceExisted) {
            throw new DeviceNotFoundException(deviceLabel);
        }

        List<Interface> interfaces = interfaceRepository.findByNetworkDeviceLabel(deviceLabel);
        return interfaceMapper.entitiesToDTOs(interfaces);
    }

    private void syncWithDB(List<Interface> newInterfaces, List<Interface> oldInterfaces, Comparator<Interface> interfaceComparator) {
        oldInterfaces.sort(interfaceComparator);
        newInterfaces.sort(interfaceComparator);
        if (oldInterfaces.equals(newInterfaces)) return;

        HashMap<Integer, Interface> integerNDInterfaceHashMap = new HashMap<>();
        for (Interface ndInterface : newInterfaces) {
            integerNDInterfaceHashMap.put(ndInterface.hashCode(), ndInterface);
        }

        for (Interface oldInterface : oldInterfaces) {
            Interface newInterface = integerNDInterfaceHashMap.get(oldInterface.hashCode());
            if (newInterface == null) {
                interfaceRepository.delete(oldInterface);
            } else {
                integerNDInterfaceHashMap.remove(oldInterface.hashCode());
            }
        }

        for (Map.Entry<Integer, Interface> keyValuePair : integerNDInterfaceHashMap.entrySet()) {
            interfaceRepository.save(keyValuePair.getValue());
        }
    }
}

package vn.com.tma.emsbackend.service.deviceinterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.comparator.InterfaceComparator;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.*;
import vn.com.tma.emsbackend.model.mapper.InterfaceMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.service.ssh.InterfaceSSHService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceRepository interfaceRepository;

    private final InterfaceMapper interfaceMapper;

//    private final NetworkDeviceService networkDeviceService;

    private final PortService portService;

    private final InterfaceSSHService interfaceSSHService;
    private final PortRepository portRepository;

    @Override
    public List<InterfaceDTO> getAll() {
        log.info("Get all interfaces");

        List<Interface> interfaces = interfaceRepository.findAll();
        return interfaceMapper.entitiesToDTOs(interfaces);
    }

    @Override
    public List<InterfaceDTO> getByNetworkDevice(Long deviceId) {
        log.info("Get all interface by device");

//        boolean checkIfDeviceExisted = networkDeviceService.existsById(deviceId);
//        if (!checkIfDeviceExisted) {
//            throw new DeviceNotFoundException(String.valueOf(deviceId));
//        }

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
        // Check device exist
//        boolean checkIfDeviceExisted = networkDeviceService.existsById(interfaceDTO.getNetworkDeviceId());
//        if (!checkIfDeviceExisted) {
//            throw new DeviceNotFoundException(String.valueOf(interfaceDTO.getNetworkDeviceId()));
//        }

        // Check duplicate name
        boolean checkIfNameExisted = interfaceRepository.existsByName(interfaceDTO.getName());
        if (checkIfNameExisted) {
            throw new InterfaceNameExistsException(interfaceDTO.getName());
        }

        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(interfaceDTO.getNetworkDeviceId());

        Port port = getValidPort(interfaceDTO);

        Interface anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);
        anInterface = interfaceRepository.save(anInterface);

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    @Transactional
    public InterfaceDTO update(long id, InterfaceDTO interfaceDTO) {
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(interfaceDTO.getNetworkDeviceId());

        // Do not update device, can update port
        Optional<Interface> interfaceOptional = interfaceRepository.findById(id);
        if (interfaceOptional.isEmpty()) {
            throw new InterfaceNotFoundException(id);
        }

        // Check duplicate name
        Interface interfaceDupName = interfaceRepository.findByName(interfaceDTO.getName());
        if (interfaceDupName != null && !interfaceDupName.getId().equals(id)) {
            throw new InterfaceNameExistsException(interfaceDTO.getName());
        }

        Port port = getValidPort(interfaceDTO);

        Interface anInterface = interfaceMapper.dtoToEntity(interfaceDTO);
        anInterface.setId(id);
        anInterface.setNetworkDevice(networkDevice);
        anInterface.setPort(port);
        anInterface = interfaceRepository.save(anInterface);

        return interfaceMapper.entityToDTO(anInterface);
    }

    @Override
    @Transactional
    public void delete(long id) {
        boolean checkIfExistedById = interfaceRepository.existsById(id);
        if (!checkIfExistedById) {
            throw new InterfaceNotFoundException(id);
        }

        interfaceRepository.deleteById(id);
    }

    private Port getValidPort(InterfaceDTO interfaceDTO) {
        Port port = null;
        if (interfaceDTO.getPortId() != null) {
            Optional<Port> portOptional = portService.getById(interfaceDTO.getPortId());

            if (portOptional.isEmpty()) {
                throw new PortNotFoundException(interfaceDTO.getPortId());
            }

            port = portOptional.get();

            if (!port.getId().equals(interfaceDTO.getNetworkDeviceId())) {
                throw new PortAndDeviceMismatchException();
            }

            if (port.getAnInterface() != null) {
                throw new PortIsAssignedException(interfaceDTO.getPortId());
            }
        }
        return port;
    }

    @Transactional
    @Override
    public void resyncInterface(long deviceId) {
        List<Interface> oldInterfaces = interfaceRepository.findByNetworkDeviceId(deviceId);
        List<Port> ports = portRepository.findByNetworkDevice_Id(deviceId);
        List<Interface> newInterfaces = interfaceSSHService.getAllInterface(deviceId, ports);

        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(deviceId);
        for (Interface ndInterface : newInterfaces) {
            ndInterface.setNetworkDevice(networkDevice);
        }

        syncWithDB(newInterfaces, oldInterfaces, new InterfaceComparator());

    }

    private void syncWithDB(List<Interface> newInterfaces, List<Interface> oldInterfaces, Comparator<Interface> interfaceComparator){
        oldInterfaces.sort(interfaceComparator);
        newInterfaces.sort(interfaceComparator);
        if (oldInterfaces.equals(newInterfaces)) return;

        HashMap<Integer, Interface> integerNDInterfaceHashMap = new HashMap<>();
        for(Interface ndInterface: newInterfaces){
            integerNDInterfaceHashMap.put(ndInterface.hashCode(), ndInterface);
        }

        for(Interface oldInterface: oldInterfaces){
            Interface newInterface =  integerNDInterfaceHashMap.get(oldInterface.hashCode());
            if(newInterface == null) {
                interfaceRepository.delete(oldInterface);
            }else{
                integerNDInterfaceHashMap.remove(oldInterface.hashCode());
            }
        }

        for(Map.Entry<Integer, Interface> keyValuePair:integerNDInterfaceHashMap.entrySet()){
            interfaceRepository.save(keyValuePair.getValue());
        }
    }
}

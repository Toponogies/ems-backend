package vn.com.tma.emsbackend.service.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.common.comparator.PortComparator;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.exception.PortNotFoundException;
import vn.com.tma.emsbackend.model.mapper.PortMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.ssh.PortSSHService;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PortServiceImpl implements PortService {
    private final PortRepository portRepository;
    private final InterfaceRepository interfaceRepository;

    private final PortMapper portMapper;

    private final NetworkDeviceService networkDeviceService;

    private final PortSSHService portSSHService;

    @Override
    public List<PortDTO> getAll() {
        log.info("Get all ports");

        List<Port> ports = portRepository.findAll();
        return portMapper.entitiesToDTOs(ports);
    }

    @Override
    public List<PortDTO> getByNetworkDevice(Long deviceId) {
        log.info("Get all ports by device: {}", deviceId);

        boolean checkIfDeviceExisted = networkDeviceService.existsById(deviceId);
        if (!checkIfDeviceExisted) {
            throw new DeviceNotFoundException(String.valueOf(deviceId));
        }

        List<Port> ports = portRepository.findByNetworkDeviceId(deviceId);
        return portMapper.entitiesToDTOs(ports);
    }

    @Override
    public PortDTO get(long id) {
        log.info("Get port with id: {}", id);

        Optional<Port> portOptional = portRepository.findById(id);
        if (portOptional.isEmpty()) {
            throw new PortNotFoundException(id);
        }
        return portMapper.entityToDTO(portOptional.get());
    }

    @Override
    public PortDTO add(PortDTO request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PortDTO update(long id, PortDTO request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long id) {
        return portRepository.existsById(id);
    }

    @Override
    @Transactional
    public void resyncPortByDeviceId(Long deviceId) {
        List<Port> newPorts = portSSHService.getAllPort(deviceId);
        List<Port> oldPorts = portRepository.findByNetworkDeviceId(deviceId);
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(deviceId);
        for (Port port : newPorts) port.setNetworkDevice(networkDevice);

        syncWithDB(newPorts, oldPorts, new PortComparator());
    }

    @Override
    public PortDTO getByNameAndNetworkDevice(String portName, String deviceLabel) {
        Port port = portRepository.findByNameAndNetworkDevice_Label(portName, deviceLabel);
        if (port == null) {
            throw new PortNotFoundException(portName, deviceLabel);
        }

        return portMapper.entityToDTO(port);
    }

    public void syncWithDB(List<Port> newPortList, List<Port> oldPortList, Comparator<Port> portComparator) {
        oldPortList.sort(portComparator);
        newPortList.sort(portComparator);
        if (oldPortList.equals(newPortList)) return;

        HashMap<Integer, Port> integerPortHashMap = new HashMap<>();
        for (Port newPort : newPortList) {
            integerPortHashMap.put(newPort.hashCode(), newPort);
        }

        for (Port oldPort : oldPortList) {
            Port newInterface = integerPortHashMap.get(oldPort.hashCode());
            if (newInterface == null) {
                interfaceRepository.deleteByPortId(oldPort.getId());
                portRepository.delete(oldPort);
            } else {
                integerPortHashMap.remove(oldPort.hashCode());
            }
        }

        for (Map.Entry<Integer, Port> keyValuePair : integerPortHashMap.entrySet()) {
            portRepository.save(keyValuePair.getValue());
        }
    }

}

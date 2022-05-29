package vn.com.tma.emsbackend.service.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.exception.PortNotFoundException;
import vn.com.tma.emsbackend.model.mapper.PortMapper;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PortServiceImpl implements PortService {
    private final PortRepository portRepository;

    private final PortMapper portMapper;

    private final NetworkDeviceService networkDeviceService;

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

        List<Port> ports = portRepository.findByNetworkDevice_Id(deviceId);
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
    public Optional<Port> getById(Long id) {
        return portRepository.findById(id);
    }

}

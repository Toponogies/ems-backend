package vn.com.tma.emsbackend.service.manageddevice;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.entity.ManagedDevice;
import vn.com.tma.emsbackend.exception.InvalidManagedDeviceIP;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.ManagedDeviceRepository;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManagedDeviceServiceImpl implements ManagedDeviceService {
    private final ManagedDeviceRepository managedDeviceRepository;
    private final Mapper mapper;

    @Override
    public List<ManagedDeviceDto> getAll() {
        log.info("Get all managed device");

        List<ManagedDevice> managedDevices = managedDeviceRepository.findAll();
        return mapper.mapList(managedDevices, ManagedDeviceDto.class);
    }

    @Override
    public ManagedDeviceDto get(long id) {
        log.info("Get managed device with id:{}", id);

        Optional<ManagedDevice> managedDeviceOptional = managedDeviceRepository.findById(id);
        if (managedDeviceOptional.isEmpty()) {
            log.info(Constant.DEVICE_NOT_FOUND + id);
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND + id);
        }
        var a = managedDeviceOptional.get();
        return mapper.map(managedDeviceOptional.get(), ManagedDeviceDto.class);
    }

    @Override
    public void delete(long id) {
        try {
            managedDeviceRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + ManagedDevice.class.getName());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND + id);
        }
    }

    @Override
    public ManagedDeviceDto add(ManagedDeviceRequestDto managedDeviceRequestDto) {
        log.info("Add new managed device");

        //check if Ip address is valid
        if (!isValidIpAddress(managedDeviceRequestDto.getIpAddress())) {
            throw new InvalidManagedDeviceIP();
        }
        ManagedDevice managedDevice = mapper.map(managedDeviceRequestDto, ManagedDevice.class);
        try {
            return mapper.map(managedDeviceRepository.save(managedDevice), ManagedDeviceDto.class);
        } catch (DataIntegrityViolationException e) {
            log.warn(Constant.CONSTRAINT_VIOLATED + ManagedDevice.class.getName());
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + ManagedDevice.class.getName());
        }
    }

    @Override
    public ManagedDeviceDto update(long id, ManagedDeviceRequestDto managedDeviceRequestDto) {
        log.info("Update managed device with id:{}", id);
        // Get device by id
        Optional<ManagedDevice> managedDeviceOptional = managedDeviceRepository.findById(id);
        if (managedDeviceOptional.isEmpty()) {
            throw new ResourceNotFoundException(Constant.DEVICE_NOT_FOUND + id);
        }
        ManagedDevice managedDevice = managedDeviceOptional.get();

        // Update data
        // check if valid ip address
        if (!isValidIpAddress(managedDeviceRequestDto.getIpAddress())) {
            throw new InvalidManagedDeviceIP();
        }
        managedDevice.setIpAddress(managedDeviceRequestDto.getIpAddress());
        managedDevice.setLabel(managedDeviceRequestDto.getLabel());
        managedDevice.setPort(managedDeviceRequestDto.getPort());

        // set new credential
        Credential credential = new Credential();
        credential.setId(managedDeviceRequestDto.getCredentialId());
        managedDevice.setCredential(credential);

        try {
            managedDeviceRepository.save(managedDevice);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(
                    Constant.CONSTRAINT_VIOLATED + ManagedDevice.class.getName());
        }
        return mapper.map(managedDevice, ManagedDeviceDto.class);
    }

    private boolean isValidIpAddress(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        if (octets.length != 4) {
            return false;
        }
        for (String octet : octets) {
            try {
                int octetValue = Integer.valueOf(octet);
                if (octetValue < 0 || octetValue > 225) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

}

package vn.com.tma.emsbackend.service.configuration;

import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;

import java.util.List;
import java.util.zip.ZipOutputStream;

public interface ConfigurationService {
    @Transactional
    byte[] downloadDeviceConfigFileById(List<Long> ids) throws DeviceNotFoundException;
}

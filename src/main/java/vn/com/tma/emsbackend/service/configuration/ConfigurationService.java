package vn.com.tma.emsbackend.service.configuration;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ConfigurationService {
    @Transactional
    void downloadDeviceConfigFileById(List<Long> ids, HttpServletResponse response);
}

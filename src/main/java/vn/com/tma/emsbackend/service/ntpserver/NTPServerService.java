package vn.com.tma.emsbackend.service.ntpserver;

import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.NTPServerDTO;
import vn.com.tma.emsbackend.service.Service;

public interface NTPServerService extends Service<NTPServerDTO> {
    @Transactional
    void  resyncNTPServer(long deviceId);
}

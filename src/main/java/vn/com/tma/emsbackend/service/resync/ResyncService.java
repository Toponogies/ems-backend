package vn.com.tma.emsbackend.service.resync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.service.ntpserver.NTPServerService;
import vn.com.tma.emsbackend.service.port.PortService;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResyncService {

    private final NetworkDeviceService networkDeviceService;

    private final PortService portService;
    private final InterfaceService interfaceService;
    private final NTPServerService ntpServerService;

    @Transactional()
    public void resync(Long id) {
        try {
            log.info("Resync network device with id " + id);
            networkDeviceService.resyncDeviceDetail(id);
            portService.resyncPort(id);
            interfaceService.resyncInterface(id);
            ntpServerService.resyncNTPServer(id);
        } catch (DeviceConnectionException e) {
            networkDeviceService.updateState(id, Enum.NetworkDeviceState.OUT_OF_SERVICE);
            log.error("", e);
        }catch(Exception e){
            log.error("Resync fail: device id:" + id , e);
        }
    }
}

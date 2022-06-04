package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.service.ntpserver.NTPServerService;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.service.ssh.utils.ResyncQueueManager;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResyncService {

    private final NetworkDeviceService networkDeviceService;
    private final PortService portService;
    private final InterfaceService interfaceService;
    private final NTPServerService ntpServerService;
    private final ResyncQueueManager resyncQueueManager;

    public void resyncDeviceById(Long id) {
        try {
            log.info("Network device with id " + id + " is resynchronizing...");
            networkDeviceService.resyncDeviceDetailById(id);
            portService.resyncPortByDeviceId(id);
            interfaceService.resyncInterfaceByDeviceId(id);
            ntpServerService.resyncNTPServerByDeviceId(id);
            log.info("Resynchronized network device with id " + id);
        } catch (DeviceConnectionException e) {
            networkDeviceService.updateStateById(id, Enum.NetworkDeviceState.OUT_OF_SERVICE);
            throw e;
        }
    }

    public void addDevicesToResyncQueueById(List<Long> ids) {
        resyncQueueManager.pushToWaitingQueue(ids.toArray(new Long[0]));
    }
}
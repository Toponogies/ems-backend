package vn.com.tma.emsbackend.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.service.ntpserver.NTPServerService;
import vn.com.tma.emsbackend.service.port.PortService;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResyncScheduler {
    private final NetworkDeviceRepository networkDeviceRepository;
    private final NetworkDeviceService networkDeviceService;
    private final PortService portService;
    private final InterfaceService interfaceService;
    private final NTPServerService ntpServerService;
    private final ResyncQueueManagement resyncQueueManagement;


//    @Scheduled(fixedRate = 10000)
    @Transactional
    public void resyncAll() {
        resyncQueueManagement.pushAll(networkDeviceRepository.findAll().stream().map(NetworkDevice::getId).collect(Collectors.toList()));
    }

//    @Scheduled(fixedRate = 100)
    @Transactional
    public void executeResync(){
        if(resyncQueueManagement.hasNext()){
            resync(resyncQueueManagement.next());
            resyncQueueManagement.pop();
        }
    }


    @Transactional
    public void resync(Long networkDeviceId){
        try {
            networkDeviceService.resyncDeviceDetail(networkDeviceId);
            portService.resyncPort(networkDeviceId);
            interfaceService.resyncInterface(networkDeviceId);
            ntpServerService.resyncNTPServer(networkDeviceId);
            log.info("Resync network device with id " + networkDeviceId);
        } catch (DeviceConnectionException e) {
            networkDeviceService.updateState(networkDeviceId, Enum.NetworkDeviceState.OUT_OF_SERVICE);
            log.error("", e);
        }
    }

}
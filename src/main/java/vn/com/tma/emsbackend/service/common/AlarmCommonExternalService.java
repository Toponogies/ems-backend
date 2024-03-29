package vn.com.tma.emsbackend.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.service.external.AlarmBaseExternalService;
import vn.com.tma.emsbackend.service.ssh.AlarmSSHService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmCommonExternalService {
    private final AlarmSSHService alarmSSHService;
    public List<Alarm> getAllAlarmByDevice(NetworkDevice networkDevice) {
        AlarmBaseExternalService alarmExternalService = getAlarmBaseExternalService(networkDevice);
        return alarmExternalService.getAllAlarmByDevice(networkDevice.getId());
    }
    private AlarmBaseExternalService getAlarmBaseExternalService(NetworkDevice networkDevice){
//        switch (networkDevice.getConnectionType()){
//            default
//        }
        return alarmSSHService;
    }
}

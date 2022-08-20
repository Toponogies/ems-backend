package vn.com.tma.emsbackend.service.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.service.common.AlarmCommonExternalService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.external.AlarmBaseExternalService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmCommonExternalService alarmCommonExternalService;
    private final NetworkDeviceService networkDeviceService;
    private final NetworkDeviceMapper networkDeviceMapper;
    public List<Alarm> getAllAlarm(Long deviceId){
        NetworkDevice networkDevice =  networkDeviceMapper.dtoToEntity(networkDeviceService.get(deviceId));
        return alarmCommonExternalService.getAllAlarm(networkDevice);
    }
}

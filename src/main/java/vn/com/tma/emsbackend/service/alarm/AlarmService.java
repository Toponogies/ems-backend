package vn.com.tma.emsbackend.service.alarm;

import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.model.entity.Alarm;

import java.util.List;

public interface AlarmService {
    List<AlarmDTO> getAllAlarmByDeviceId(Long deviceId);

    List<AlarmDTO> getAllAlarm();

    void sendNewAlarmWebSocket(Alarm alarm, String ipAddress);
}
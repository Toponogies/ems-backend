package vn.com.tma.emsbackend.service.alarm;

import vn.com.tma.emsbackend.model.dto.AlarmDTO;

import java.util.List;

public interface AlarmService {
    List<AlarmDTO> getAllAlarmByDeviceLabel(String label);

    List<AlarmDTO> getAllAlarmByDeviceId(Long deviceId);

    List<AlarmDTO> getAllAlarm();
}
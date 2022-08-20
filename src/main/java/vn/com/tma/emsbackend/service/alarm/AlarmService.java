package vn.com.tma.emsbackend.service.alarm;

import vn.com.tma.emsbackend.model.entity.Alarm;

import java.util.List;

public interface AlarmService {
    List<Alarm> getAllAlarm(Long deviceId);
}
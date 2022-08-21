package vn.com.tma.emsbackend.service.external;

import vn.com.tma.emsbackend.model.entity.Alarm;

import java.util.List;

public interface AlarmBaseExternalService {
    List<Alarm> getAllAlarmByDevice(Long deviceId);

}

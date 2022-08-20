package vn.com.tma.emsbackend.service.external;

import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;

import java.util.List;

public interface AlarmBaseExternalService {
    List<Alarm> getAllAlarm(Long deviceId);

}

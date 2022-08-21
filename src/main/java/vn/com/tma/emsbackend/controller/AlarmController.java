package vn.com.tma.emsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.model.mapper.AlarmMapper;
import vn.com.tma.emsbackend.service.alarm.AlarmService;

import java.util.List;


@RequestMapping("/alarms")
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final AlarmMapper alarmMapper;

    @GetMapping("/{id}")
    List<AlarmDTO> getAllAlarmByDeviceId(@PathVariable(value = "id") Long deviceId) {
        return alarmService.getAllAlarmByDeviceId(deviceId);
    }

    @GetMapping()
    List<AlarmDTO> getAllAlarm(){
        return alarmService.getAllAlarm();
    }
}

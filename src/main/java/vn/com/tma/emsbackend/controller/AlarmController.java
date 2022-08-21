package vn.com.tma.emsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.service.alarm.AlarmService;

import java.util.List;


@RequestMapping("/alarms")
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/devices/{id}")
    List<AlarmDTO> getAllAlarmByDeviceId(@PathVariable(value = "id") Long deviceId) {
        return alarmService.getAllAlarmByDeviceId(deviceId);
    }

    @GetMapping("/devices/label/{label}")
    List<AlarmDTO> getAllAlarmByDeviceId(@PathVariable(value = "label") String label) {
        return alarmService.getAllAlarmByDeviceLabel(label);
    }

    @GetMapping()
    List<AlarmDTO> getAllAlarm(){
        return alarmService.getAllAlarm();
    }
}

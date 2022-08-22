package vn.com.tma.emsbackend.model.entity;

import lombok.Data;
import vn.com.tma.emsbackend.common.enums.Enum;

@Data
public class Alarm {
    private String date;
    private Enum.AlarmSeverity alarmSeverity;
    private String condition;
    private String description;
    private String alarmNumber;
}

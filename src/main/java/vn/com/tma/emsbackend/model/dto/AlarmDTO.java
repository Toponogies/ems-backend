package vn.com.tma.emsbackend.model.dto;

import lombok.Data;
import vn.com.tma.emsbackend.common.enums.Enum;

@Data
public class AlarmDTO {
    private String date;
    private Enum.AlarmSeverity alarmSeverity;
    private String condition;
    private String description;
    private String alarmNumber;
    private String networkDevice;
}

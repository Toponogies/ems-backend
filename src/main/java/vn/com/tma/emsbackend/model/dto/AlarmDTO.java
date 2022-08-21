package vn.com.tma.emsbackend.model.dto;

import lombok.Data;
import vn.com.tma.emsbackend.common.enums.Enum;

import java.util.Date;

@Data
public class AlarmDTO {
    private Date date;
    private Enum.Severity severity;
    private String condition;
    private String description;
    private String alarmNumber;
}

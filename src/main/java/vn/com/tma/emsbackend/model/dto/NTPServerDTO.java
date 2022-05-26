package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.enums.Enum;

import javax.persistence.Column;

@Getter
@Setter
public class NTPServerDTO {
    private Long id;

    private String serverAddress;

    private Enum.State state;

    private Long networkDeviceId;
}

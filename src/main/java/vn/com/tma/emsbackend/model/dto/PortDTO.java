package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortDTO {
    private Long id;

    private String connector;

    private String macAddress;

    private String name;

    private String state;

    private Long networkDeviceId;
}

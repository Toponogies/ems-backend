package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PortDTO {
    private Long id;

    private String connector;

    private String macAddress;

    @NotNull(message = "Name field can not empty")
    private String name;

    private String state;

    @NotNull(message = "Port must belong to a device")
    private Long networkDeviceId;
}
